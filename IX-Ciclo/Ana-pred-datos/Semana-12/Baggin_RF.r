#------------------------------------------------------------------#
#                             LIBRERÍAS                            #
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

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-12/")

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

#------------------------------------------------------------------#
#                DATOS DE ENTRENAMIENTO Y DE PRUEBA                #
#------------------------------------------------------------------#

RNGkind(sample.kind = "Rounding")

set.seed(100)

# Credito es la variable objetivo
indice <- createDataPartition(datos$Credito, p = 0.7, list = FALSE)

data_train <- datos[indice, ]
dim(data_train)

data_test <- datos[-indice, ]
dim(data_test)

#==================================================================#
#                       EL ALGORITMO BAGGING                       #
#==================================================================#

RNGkind(sample.kind = "Rounding")

set.seed(100)

# cv es cross-validation
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
  lty = 2, lwd = 1.8,
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

RNGkind(sample.kind = "Rounding")

set.seed(100)

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
# ---------
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

# Nuevas predicciones
# -------------------
nuevos <- read_excel(
  path = "./Credito.xlsx",
  sheet = 2,
  range = "A1:L31",
  col_names = TRUE
)

head(nuevos, 10)

nuevos$Educacion <- ordered(
  nuevos$Educacion,
  c("No.Sup", "Sup.Incomp", "Sup.Comp")
)

pred_nuevos <- predict(modelo_rf, newdata = nuevos, type = "prob")

final <- data.frame(pred_nuevos, nuevos)
final

pred_nuevos <- predict(modelo_rf, newdata = nuevos, type = "raw")

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
saveRDS(modelo_rf, "./resultados/modelo_rf.rds")

# Leer el modelo final
# --------------------
super_model <- readRDS("./resultados/modelo_rf.rds")
print(super_model)

# Hacer predicciones
# ------------------
final_predictions <- predict(super_model, nuevos)
final_predictions
