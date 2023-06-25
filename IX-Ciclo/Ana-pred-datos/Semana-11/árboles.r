#------------------------------------------------------------------#
#                             LIBRERíAS                            #
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
library(readxl)

#------------------------------------------------------------------#
#                     CARGANDO LA BASE DE DATOS                    #
#------------------------------------------------------------------#

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-11/")

datos <- read_excel(
  path = "./Credito.xlsx",
  sheet = 1,
  range = "A1:M751",
  col_names = TRUE
)

head(datos, 10)

str(datos)

# Definimos que la variable Eduación es de tipo ordinal (jerárquico)
datos$Educacion <- ordered(
  datos$Educacion,
  c("No.Sup", "Sup.Incomp", "Sup.Comp")
)

str(datos)

#------------------------------------------------------------------#
#                DATOS DE ENTRENAMIENTO Y DE PRUEBA                #
#------------------------------------------------------------------#

set.seed(1983)

# Credito es la variable objetivo
indice <- createDataPartition(datos$Credito, p = 0.7, list = FALSE)

data_train <- datos[indice, ]
dim(data_train)

data_test <- datos[-indice, ]
dim(data_test)

#==================================================================#
#                          ALGORITMO CART                          #
#==================================================================#

# Árbol completo
# --------------
set.seed(200)

arbol_completo <- rpart(
  Credito ~ .,
  data = data_train,
  method = "class",
  cp = 0,
  minbucket = 0
)

rpart.plot(
  arbol_completo,
  digits = -1,
  type = 2,
  extra = 101,
  cex = 0.7,
  nn = TRUE
)

printcp(arbol_completo)

# Predicción árbol completo
# -------------------------
pred1 <- predict(arbol_completo, data_test, type = "class")

# Calculando el accuracy
# ----------------------
accuracy <- mean(pred1 == data_test$Credito)
accuracy

# Eligiendo el CP adecuado y podando el árbol
# -------------------------------------------
minxerr <- which.min(arbol_completo$cptable[, "xerror"])

min_cp <- arbol_completo$cptable[minxerr, "CP"]
min_cp

arbol_pruned <- prune(arbol_completo, cp = min_cp)
arbol_pruned

rpart.plot(
  arbol_pruned,
  digits = -1,
  type = 2,
  extra = 101,
  cex = 0.7,
  nn = TRUE
)

# Predicción árbol podado
# -----------------------
pred2 <- predict(arbol_pruned, data_test, type = "class")

# Calculando el accuracy
# ----------------------
mean(pred2 == data_test$Credito)

# CART con el paquete caret y validación cruzada
# ----------------------------------------------
# Relación de modelos
library(caret)

names(getModelInfo())

# Relación de parámetros a ajustar de un modelo
# Nos dice qué parámetro ajustar
modelLookup(model = "rpart")

# Aplicando el modelo con Validación Cruzada Repetida
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

modelo_cart

modelo_cart$bestTune

plot(modelo_cart)

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

# Predicción correcta y error
# ---------------------------
accuracy <- mean(data_test$Credito == pred_cart)
accuracy

error <- mean(data_test$Credito != pred_cart)
error

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

# Vemos que con las 5 primeras variables es suficiente
plot(impor)

#------------------------------------------------------------------#
#                       EL ALGORITMO BAGGING                       #
#------------------------------------------------------------------#

set.seed(200)

ctrl <- trainControl(method = "cv", number = 10)

modelo_bag <- train(Credito ~ .,
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

# Predicción correcta y error
# ---------------------------
accuracy <- mean(data_test$Credito == pred_bag)
accuracy

error <- mean(data_test$Credito != pred_bag)
error

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

#==================================================================#
#                    EL ALGORITMO RANDOM FOREST                    #
#==================================================================#

set.seed(200)

modelLookup(model = "rf")

ctrl <- trainControl(method = "cv", number = 10)

modelo_rf <- train(Credito ~ .,
  data = data_train,
  method = "rf",
  trControl = ctrl,
  tuneLength = 5,
  metric = "Accuracy"
)

modelo_rf

pred_rf <- predict(modelo_rf, newdata = data_test[, -1], type = "raw")

confusionMatrix(
  data = pred_rf,
  reference = as.factor(data_test$Credito),
  positive = "Si"
)

# Predicción correcta y error
# ---------------------------
accuracy <- mean(data_test$Credito == pred_rf)
accuracy

error <- mean(data_test$Credito != pred_rf)
error

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

# Comparando el entrenamiento en los modelos
modelos <- list(
  CART = modelo_cart,
  Bagging = modelo_bag,
  Random_Forest = modelo_rf
)

comparacion_modelos <- resamples(modelos)

resumen <- summary(comparacion_modelos)
resumen

bwplot(comparacion_modelos)

densityplot(comparacion_modelos, metric = "Accuracy", auto.key = TRUE)

# Curvas ROC
# Formato 1
# -----------
library(ROCR)
roc.curve(
  data_test$Credito,
  pred_cart2[, 2],
  lty = 2,
  lwd = 1.8,
  col = "red"
)

roc.curve(
  data_test$Credito,
  pred_bag2[, 2],
  add.roc = TRUE,
  lty = 2,
  lwd = 1.8, col = "green"
)

roc.curve(
  data_test$Credito,
  pred_rf2[, 2],
  add.roc = TRUE,
  lwd = 2,
  col = "blue"
)

legend(
  "bottomright",
  names(modelos),
  col = c("red", "green", "blue"),
  lwd = 1
)

# Formato 2
# -----------
curvas <- function(modelos, datos_test) {
  test_prob <- function(model, data) {
    list(predict(model, data, type = "prob")[, 2])
  }

  prob_list <- modelos %>% map(test_prob, data = datos_test)

  mscurves <- evalmod(
    mmdata(prob_list, datos_test[, 1], modnames = names(prob_list))
  )

  aucs <- data.frame(subset(auc(mscurves), curvetypes == "ROC")[, -c(2:3)])

  names(aucs) <- c("Modelos", "ROC-auc")

  autoplot(mscurves)

  print(aucs)
}
curvas(modelos, data_test)


# Comparación
# -----------
test_ac <- function(model, data) {
  mean(predict(model, data, type = "raw") == data[, 1])
}

model_list_ac <- modelos %>% map(test_ac, data = data_test)

l <- data.frame(melt(model_list_ac)[, c(2, 1)])

acur <- cbind(data.frame(resumen$statistics$Accuracy)[, 4])

comparacion <- data.frame(l, acur)[, c(1, 3, 2)]

names(comparacion) <- c("Modelos", "Accuracy CV", "Accuracy")

comparacion

d <- melt(comparacion)

ggplot(data = d, aes(x = reorder(Modelos, value), y = value, fill = variable)) +
  geom_bar(stat = "identity", position = position_dodge()) +
  geom_text(aes(label = round(value, 3)),
    vjust = 0.5, hjust = 1.5, color = "white",
    position = position_dodge(0.9), size = 3.5
  ) +
  scale_fill_brewer(palette = "Paired") +
  coord_flip() +
  theme_minimal() +
  labs(title = "Rsquare Train-Test Vs Rsquare CV") +
  xlab("Modelos") +
  ylab("Accuracy")


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

# Guardar cualquier archivo
# -------------------------
if (!dir.exists("./resultados/")) dir.create("./resultados/")

write.table(
  final,
  file = "./resultados/predicciones.txt",
  sep = "\t",
  eol = "\n",
  dec = ".",
  row.names = FALSE,
  col.names = TRUE
)

# Guardar modelo en UCV O DISCO
# -----------------------------
saveRDS(modelo_bag, "./modelo_boos.rds")

# Leer el modelo final
# --------------------
super_model <- readRDS("./modelo_bag.rds")
print(super_model)

# Hacer predicciones
# ------------------
final_predictions <- predict(super_model, nuevos)
final_predictions
