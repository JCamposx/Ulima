#------------------------------------------------------------------#
#                            LIBRERIAS                             #
#------------------------------------------------------------------#

library(ggplot2)  # Gráficas
library(ggpubr)   # Conectar a las base de datos
library(reshape2) # Condensar base de datos
library(dplyr)    # Resumenes de datos
library(caret)    # Partición y modelamiento
library(purrr)    # Enlaces
library(ROSE)     # Curvas Roc
library(readxl)   # Leer archivos excel

#------------------------------------------------------------------#
#                     CARGANDO LA BASE DE DATOS                    #
#------------------------------------------------------------------#

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-14/")

datos <- read_excel(
  path = "./Credito.xlsx",
  sheet = 1,
  range = "A1:H751",
  col_names = TRUE
)

head(datos, 10)

datos$Credito <- as.factor(datos$Credito)

str(datos)

datos_x <- scale(datos[, -1]) # Menos a la columna 1 porque es la objetivo

credito <- datos[, 1] # La columna 1 es la objetivo

datos <- data.frame(credito, datos_x)

View(datos)

str(datos)

#------------------------------------------------------------------#
#                       PARTICIONANDO LA DATA                      #
#------------------------------------------------------------------#

RNGkind(sample.kind = "Rounding")

set.seed(100)

indice <- createDataPartition(datos$Credito, p = 0.7, list = FALSE)

data_train <- datos[indice, ]
dim(data_train)

data_test <- datos[-indice, ]
dim(data_test)

head(data_train)

table(data_train$Credito) # Vemos que los datos no están desbalanceados

#------------------------------------------------------------------#
#                         REDES NEURONALES                         #
#------------------------------------------------------------------#

modelLookup("nnet")

set.seed(100)

ctrl <- trainControl(method = "cv", number = 10)

modelo_net <- train(
  Credito ~ .,
  data = data_train,
  method = "nnet",
  trControl = ctrl,
  linout = FALSE,
  tunegrid = expand.grid(size = 2:10, decay = c(0, 0.001, 0.01)),
  trace = FALSE,
  maxit = 200
)

ggplot(modelo_net, highlight = TRUE)

library(NeuralNetTools)
plotnet(modelo_net$finalModel)

pred_net <- predict(modelo_net, newdata = data_test[, -1], type = "raw")

table(pred_net, data_test$Credito)

confusionMatrix(
  data = pred_net,
  reference = as.factor(data_test$Credito),
  positive = "Si"
)

# Predicción correcta y error
# ---------------------------
accuracy <- mean(data_test$Credito == pred_net)
accuracy

error <- mean(data_test$Credito != pred_net)
error

# Curva ROC
# ---------
pred_net2 <- predict(modelo_net, newdata = data_test[, -1], type = "prob")

roc.curve(
  data_test$Credito,
  pred_net2[, 2],
  lty = 2,
  lwd = 1.8,
  col = "blue",
  main = "ROC curves"
)

# Importancia de Variables
# ------------------------
impor <- varImp(modelo_net)
impor

plot(impor)

nuevos <- read_excel(
  path = "./Credito.xlsx",
  sheet = 2,
  range = "A1:G6",
  col_names = TRUE
)

nuevos <- scale(nuevos)
nuevos

pred <- predict(modelo_net, newdata = nuevos, type = "raw")

data.frame(pred, nuevos)
