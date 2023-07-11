#------------------------------------------------------------------#
#                             LIBRERIAS                            #
#------------------------------------------------------------------#

library(ggplot2)
library(reshape)
library(ggpubr)
library(reshape2)
library(dplyr)
library(caret)
library(rpart)
library(rpart.plot)
library(purrr)
library(precrec)
library(ROSE)
library(ROCR)
library(randomForest)
library(ada)
library(readxl)

#------------------------------------------------------------------#
#                     CARGANDO LA BASE DE DATOS                    #
#------------------------------------------------------------------#

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-13/")

datos <- read_excel(
  path = "./Credito.xlsx",
  sheet = 1,
  range = "A1:M751",
  col_names = TRUE
)

head(datos, 10)

str(datos)

datos$Educacion <- ordered(
  datos$Educacion,
  c("No.Sup", "Sup.Incomp", "Sup.Comp")
)

#------------------------------------------------------------------#
#                DATOS DE ENTRENAMIENTO Y DE PRUEBA                #
#------------------------------------------------------------------#

RNGkind(sample.kind = "Rounding")

set.seed(100)

indice <- createDataPartition(datos$Credito, p = 0.7, list = FALSE)

data_train <- datos[indice, ]
dim(data_train)

data_test <- datos[-indice, ]
dim(data_test)

#------------------------------------------------------------------#
#                       EL ALGORITMO ADABOOST                      #
#------------------------------------------------------------------#

set.seed(200)

ctrl <- trainControl(method = "cv", number = 10)

modelo_boos <- train(
  Credito ~ .,
  data = data_train,
  method = "ada",
  tuneLength = 6,
  trControl = ctrl,
  metric = "Accuracy"
)

plot(modelo_boos)

modelo_boos$bestTune

pred_boos <- predict(modelo_boos, newdata = data_test[, -1], type = "raw")

confusionMatrix(
  data = pred_boos,
  reference = factor(data_test$Credito),
  positive = "Si"
)


# Predicción correcta y error
# ---------------------------
accuracy <- mean(data_test$Credito == pred_boos)
accuracy

error <- mean(data_test$Credito != pred_boos)
error

# Curva ROC
# -----------
pred_boos2 <- predict(modelo_boos, newdata = data_test[, -1], type = "prob")

roc.curve(
  data_test$Credito,
  pred_boos2[, 2],
  lty = 2,
  lwd = 1.8,
  col = "blue",
  main = "ROC curves"
)

# Importancia de Variables
# -------------------------
impor <- varImp(modelo_boos)
impor

plot(impor)

#------------------------------------------------------------------#
#                         EL ALGORITMO CART                        #
#------------------------------------------------------------------#

# Aplicando el modelo con Validación Cruzada Repetida
# ---------------------------------------------------
set.seed(200)

ctrl <- trainControl(method = "cv", number = 10)

# classProbs=TRUE,summaryFunction = twoClassSummary)

modelo_cart <- train(
  Credito ~ .,
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
  reference = as.factor(data_test$Credito),
  positive = "Si"
)

# Curva ROC
# -----------
pred_cart2 <- predict(modelo_cart, newdata = data_test[, -1], type = "prob")

roc.curve(
  data_test$Credito,
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
#                       EL ALGORITMO BAGGING                       #
#------------------------------------------------------------------#

RNGkind(sample.kind = "Rounding")

set.seed(100)

ctrl <- trainControl(method = "cv", number = 10)

modelo_bag <- train(
  Credito ~ .,
  data = data_train,
  method = "treebag",
  trControl = ctrl,
  tuneLength = 5,
  metric = "Accuracy"
)

modelo_bag

pred_bag <- predict(modelo_bag, newdata = data_test[, -1], type = "raw")

confusionMatrix(
  data = pred_bag,
  reference = as.factor(data_test$Credito),
  positive = "Si"
)

# Curva ROC
# -----------
pred_bag2 <- predict(modelo_bag, newdata = data_test[, -1], type = "prob")

roc.curve(
  data_test$Credito,
  pred_bag2[, 2],
  lty = 2,
  lwd = 1.8,
  col = "blue",
  main = "ROC curves"
)

# Importancia de Variables
# -------------------------
impor <- varImp(modelo_bag)
impor

plot(impor)

#------------------------------------------------------------------#
#                   EL ALGORITMO RANDOM FOREST                     #
#------------------------------------------------------------------#

RNGkind(sample.kind = "Rounding")

set.seed(100)

modelLookup(model = "rf")

ctrl <- trainControl(method = "cv", number = 10)

modelo_rf <- train(
  Credito ~ .,
  data = data_train,
  method = "rf",
  trControl = ctrl,
  tuneLength = 5,
  metric = "Accuracy"
)

pred_rf <- predict(modelo_rf, newdata = data_test[, -1], type = "raw")

confusionMatrix(
  data = pred_rf,
  reference = as.factor(data_test$Credito),
  positive = "Si"
)

# Curva ROC
# -----------
pred_rf2 <- predict(modelo_rf, newdata = data_test[, -1], type = "prob")

roc.curve(
  data_test$Credito,
  pred_rf2[, 2],
  lty = 2,
  lwd = 1.8,
  col = "blue",
  main = "ROC curves"
)

# Importancia de Variables
# -------------------------
impor <- varImp(modelo_rf)
impor

plot(impor)

#------------------------------------------------------------------#
#                     COMPARACIÓN DE LOS MDELOS                    #
#------------------------------------------------------------------#

# Comparando el entrenamiento en los modelos
# ------------------------------------------
modelos <- list(
  CART = modelo_cart,
  Bagging = modelo_bag,
  Random_Forest = modelo_rf,
  AdaBoosting = modelo_boos
)

comparacion_modelos <- resamples(modelos)

resumen <- summary(comparacion_modelos)
resumen

bwplot(comparacion_modelos)

dotplot(comparacion_modelos)

densityplot(comparacion_modelos, metric = "Accuracy", auto.key = TRUE)

# Correlación entre los resultados
# --------------------------------
modelCor(comparacion_modelos)

splom(comparacion_modelos)

# Curvas ROC
# Formato 1
# -----------
modelos <- list(
  CART = modelo_cart,
  Bagging = modelo_bag,
  Random_Forest = modelo_rf,
  AdaBoosting = modelo_boos
)

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
    subset(precrec::auc(mscurves), curvetypes == type)[, -c(2:3)]
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

# library(conflicted)
conflict_prefer("autoplot", "ggplot2")

res <- curves_roc_prc(modelos, data_test, type = "ROC")

res$curvas

# Comparación
# -----------
test_ac <- function(model, data) {
  mean(predict(model, data, type = "raw") == data[, 1])
}

model_list_ac <- modelos %>% map(test_ac, data = data_test)

l <- data.frame(melt(model_list_ac)[, c(2, 1)])

acur <- cbind(
  data.frame(resumen$statistics$Accuracy)[, 4],
  resumen$statistics$Kappa[, 4]
)

comparacion <- data.frame(l, acur)

names(comparacion) <- c("Modelos", "Accuracy", "Accuracy CV", "Kappa CV")

comparacion <- merge(comparacion, res$auc, by.x = "Modelos", by.y = "Modelos")
comparacion

d <- melt(comparacion)
# d <- reshape2::melt(comparación)

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

predict(
  modelo_boos,
  data.frame(
    Sexo = "M",
    E.Civil = "Casado",
    Educacion = "Sup.Incomp",
    Prioridad = "Si",
    Lic.Conducir = "Si",
    Edad = 40,
    Tarjetas = 3,
    Deuda = 0,
    Saldo = 5000,
    CrediScore = 0.50,
    Años_empleo = 5,
    Ingresos = 4500
  ),
  type = "prob"
)

# Nuevas predicciones
# -------------------
nuevos <- read.delim("clipboard")

nuevos$Educacion <- ordered(
  nuevos$Educacion,
  c("No.Sup", "Sup.Incomp", "Sup.Comp")
)

pred_nuevos <- predict(modelo_bag, newdata = nuevos, type = "prob")

final <- data.frame(pred_nuevos, nuevos)
final

pred_nuevos <- predict(modelo_bag, newdata = nuevos, type = "raw")

final <- data.frame(pred_nuevos, nuevos)
final