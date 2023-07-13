#------------------------------------------------------------------#
#                             LIBRERIAS                            #
#------------------------------------------------------------------#

library(ggplot2) # Gráficas
library(ggpubr) # Conectar a las base de datos
library(reshape2) # Condensar base de datos
library(dplyr) # Resumenes de datos
library(caret) # Partición y modelamiento
library(purrr) # Enlaces
library(ROSE) # Curvas Roc
library(rpart)
library(rpart.plot)
library(precrec)
library(readxl)

#------------------------------------------------------------------#
#                     CARGANDO LA BASE DE DATOS                    #
#------------------------------------------------------------------#

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-15/Redes-neuronales")

datos <- read_excel(
  path = "./VENTAS.xlsx",
  sheet = 1,
  range = "A2:D124",
  col_names = TRUE
)

head(datos, 10)

str(datos)

datos$Ventas <- as.factor(datos$Ventas)

str(datos)

datos_x <- scale(datos[, -1]) # Estandarizamos las X

ventas <- datos[, 1] # La variable objetivo, la Y

datos <- data.frame(ventas, datos_x) # Juntamos las X estandarizadas con la Y

head(datos)

str(datos)

#------------------------------------------------------------------#
#                       PARTICIONANDO LA DATA                      #
#------------------------------------------------------------------#

RNGkind(sample.kind = "Rounding")
set.seed(100)

indice <- createDataPartition(datos$Ventas, p = 0.7, list = FALSE)

data_train <- datos[indice, ]
dim(data_train)
head(data_train)

data_test <- datos[-indice, ]
dim(data_test)
head(data_test)

table(data_train$Ventas)
table(data_test$Ventas)

#------------------------------------------------------------------#
#                         REDES NEURONALES                         #
#------------------------------------------------------------------#

modelLookup("nnet")

set.seed(100)

ctrl <- trainControl(method = "cv", number = 10)

modelo_net <- train(
  Ventas ~ .,
  data = data_train,
  method = "nnet",
  trControl = ctrl,
  linout = FALSE,
  tunegrid = expand.grid(
    size = 2:10, decay = c(0, 0.001, 0.01)
  ),
  trace = FALSE, maxit = 200
)

ggplot(modelo_net, highlight = TRUE)

library(NeuralNetTools)
plotnet(modelo_net$finalModel)

pred_net <- predict(modelo_net, newdata = data_test, type = "raw")

table(pred_net, data_test$Ventas)

confusionMatrix(
  data = pred_net,
  reference = as.factor(data_test$Ventas),
  positive = "Si"
)

# Predicción correcta y error
# ---------------------------
accuracy <- mean(data_test$Ventas == pred_net)
accuracy

error <- mean(data_test$Ventas != pred_net)
error

# Curva ROC
# -----------
pred_net2 <- predict(modelo_net, newdata = data_test[, -1], type = "prob")

roc.curve(
  data_test$Ventas,
  pred_net2[, 2],
  lty = 2,
  lwd = 1.8,
  col = "blue",
  main = "ROC curves"
)

# Importancia de Variables
# -------------------------
impor <- varImp(modelo_net)
impor

plot(impor)

#------------------------------------------------------------------#
#                         EL ALGORITMO CART                        #
#------------------------------------------------------------------#

# Aplicando el modelo con Validación Cruzada Repetida
set.seed(100)

ctrl <- trainControl(method = "cv", number = 10)

# classProbs=TRUE,summaryFunction = twoClassSummary)

modelo_cart <- train(
  Ventas ~ .,
  data = data_train,
  method = "rpart",
  trControl = ctrl,
  tuneGrid = expand.grid(cp = seq(0, 0.5, 0.001)),
  metric = "Accuracy"
)

rpart.plot(
  modelo_cart$finalModel,
  roundint = FALSE,
  digits = -3,
  type = 1,
  extra = 101,
  cex = .7,
  nn = TRUE
)

pred_cart <- predict(modelo_cart, newdata = data_test[, -1], type = "raw")

confusionMatrix(
  data = pred_cart,
  reference = as.factor(data_test$Ventas),
  positive = "Si"
)

# Curva ROC
# -----------
pred_cart2 <- predict(modelo_cart, newdata = data_test[, -1], type = "prob")

roc.curve(
  data_test$Ventas,
  pred_cart2[, 2],
  lty = 2,
  lwd = 1.8,
  col = "blue",
  main = "ROC curves"
)

# Importancia de Variables
# -------------------------
impor <- varImp(modelo_cart)
impor

plot(impor)

#------------------------------------------------------------------#
#                     COMPARACIÓN DE RESULTADOS                    #
#------------------------------------------------------------------#

# Comparando el entrenamiento en los modelos
modelos <- list(
  CART = modelo_cart,
  NET = modelo_net
)

comparacion_modelos <- resamples(modelos)

resumen <- summary(comparacion_modelos)
resumen

bwplot(comparacion_modelos)

dotplot(comparacion_modelos)

densityplot(comparacion_modelos, metric = "Accuracy", auto.key = TRUE)

# Correlation between results
modelCor(comparacion_modelos)
splom(comparacion_modelos)

# Curvas ROC
# Formato 1
# -----------
curves_roc_prc <- function(modelos, datos_test, type) {
  modelos <- modelos

  datos_test <- data_test

  type <- "ROC"

  test_prob <- function(model, data) {
    list(1 - data.frame(d = predict(model, data, type = "prob"))[1])
  }

  prob_list <- modelos %>% map(test_prob, data = datos_test)

  mscurves <- evalmod(
    mmdata(
      prob_list,
      datos_test[, 1],
      modnames = names(prob_list)
    )
  )

  aucs <- data.frame(
    subset(
      precrec::auc(mscurves),
      curvetypes == type
    )[, -c(2:3)]
  )

  names(aucs) <- c("Modelos", "ROC-auc")

  aucs <- aucs[order(aucs$`ROC-auc`, decreasing = TRUE), ]

  stable_p <- ggtexttable(aucs, theme = ttheme("classic"), rows = NULL)

  curv <- autoplot(mscurves, curvetype = type) +
    ggtitle(paste("Curvas ", type, "(AUC) para los modelos")) +
    geom_line(size = 0.8) +
    theme(legend.position = "top", plot.title = element_text(hjust = 0.5)) +
    # if(type == "ROC") {
    annotation_custom(ggplotGrob(stable_p),
      xmin = 0.7, ymin = 0.1,
      ymax = 0.4, xmax = 0.9
    ) +
    geom_abline(
      intercept = 0, slope = 1, color = "black", size = 0.7,
      linetype = 2
    )
  # } else {annotation_custom(ggplotGrob(stable.p),
  # xmin = 0.4, ymin = 0.05, ymax = 0.4, xmax = 0)}
  return(c(curvas = list(curv), auc = list(aucs)))
}

library(conflicted)
conflict_prefer("autoplot", "ggplot2")

res <- curves_roc_prc(modelos, data_test, type = "ROC")

res$curvas

evalmod

# Comparación
# -----------
test_ac <- function(model, data) {
  mean(predict(model, data, type = "raw") == data[, 1])
}

model_list_ac <- modelos %>% map(test_ac, data = data_test)

l <- data.frame(reshape2::melt(model_list_ac)[, c(2, 1)])

acur <- cbind(
  data.frame(resumen$statistics$Accuracy)[, 4],
  resumen$statistics$Kappa[, 4]
)

comparacion <- data.frame(l, acur)

names(comparacion) <- c("Modelos", "Accuracy", "Accuracy CV", "Kappa CV")

comparacion <- merge(comparacion, res$auc, by.x = "Modelos", by.y = "Modelos")
comparacion

d <- reshape2::melt(comparacion)

ggplot(data = d, aes(x = reorder(Modelos, value), y = value, fill = variable)) +
  geom_bar(stat = "identity", position = position_dodge()) +
  geom_text(
    aes(label = round(value, 3)),
    vjust = 0.5,
    hjust = 1.5,
    color = "white",
    position = position_dodge(0.9),
    size = 3.2
  ) +
  scale_fill_brewer(palette = "Paired") +
  coord_flip() +
  labs(title = "Comparación de  diferentes métricas") +
  xlab("Modelos") +
  ylab("Métricas") +
  theme(legend.title = element_blank(), plot.title = element_text(hjust = 0.5))

#------------------------------------------------------------------#
#                           PREDICCIONES                           #
#------------------------------------------------------------------#

head(datos)

predict(modelo_net, data.frame(
  Televisión = 250,
  Radio = 60,
  Redes = 40
))

predict(modelo_cart, data.frame(
  Televisión = 250,
  Radio = 60,
  Redes = 40
))
