# ==================================================================#
#                    ANÁLISIS EXPLORATORIO DE DATOS                #
# ==================================================================#

library(dplyr)
library(VIM)
library(DMwR2)
library(missForest)
library(GGally)
library(ggplot2)
library(corrplot)
library(corrr)
library(vioplot)
library(reshape2)
library(aplpack)
library(fmsb)
library(readxl)


#------------------------------------------------------------------#
#  Cargando la base de datos                                       #
#------------------------------------------------------------------#

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-3/")

gasto <- read_excel(
  path = "./Gastos.xlsx",
  range = "B2:F152",
  col_names = TRUE
)

print(gasto, n = 150)

dim(gasto)
# Vemos que tenemos 150 filas x 5 columnas
# Esto quiere decir que son 150 observaciones y 5 variables

#------------------------------------------------------------------#
#  Imputación de datos                                             #
#------------------------------------------------------------------#

# Eliminacion de casos
# -------------------
# Obtenemos cantidad de valores NA, vemos que hay 6
sum(is.na(gasto))

# Eliminamos las filas con NA
gasto1 <- na.omit(gasto)

# Vemos que tenemos 6 filas menos
dim(gasto1)

# Vemos que ahora no hay ningún NA
sum(is.na(gasto1))

# Con librería DMwR
#------------------
# Llenamos los valores NA con el promedio de toda la columna de la variable
gasto2 <- centralImputation(gasto)
print(gasto2, n = 150)

# Vemos que se siguen manteniendo las 150 filas x 5 columnas
dim(gasto2)

# Vemos que no hay valores NA
sum(is.na(gasto2))

# Con librería VIM
#----------------
# Lo mismo que DMwR, pero nosotros decidimos el método para imputar los datos
gasto3 <- initialise(gasto, method = "median")

sum(is.na(gasto3))

sum(is.na(gasto))

# OJO: Esto de imputar datos se realiza cuando los valores perdidos son pocos,
# de como mucho un 5% o por ahí. Cuando los valores perdidos son muchos, hay que
# usar modelos, como Random Forest.

# Imputar valores missing usando el algoritmo Random Forest
#----------------------------------------------------------
# Imputación de datos haciendo uso de Random Forest
gasto4 <- missForest(gasto[, -5])$ximp

sum(is.na(gasto4))

# OJO: El [, -5] es para quitar la columna 5, que es la de Ciudad, ya que
# contiene datos cualitativos

#------------------------------------------------------------------#
#  1. NOTACIONES Y DEFINICIONES MATEMÁTICAS                        #
#------------------------------------------------------------------#

# vector de medias
# -----------------
# Es el promedio de cada columna
vmed <- colMeans(gasto2[, -5])
vmed
# Vemos que el mayor gasto está en alimentos, y el menor en recreación

# Matriz de correlacones
# ---------------------------------
# Es la correlación que existen entre todas las variables
corre <- cor(gasto2[, -5])
corre


#------------------------------------------------------------------#
#    2. GRÁFICOS EXPLORATORIOS                                     #
#------------------------------------------------------------------#
boxplot(gasto2[, -5])

vioplot(gasto2[, -5])
# Vemos que alimentos y servicios son más o menos normales, son más o menos
# simétricos, probablemente tenga distribución normal.
# Pero Educacion y Recreación no son normales, vemos que hay sesgo.
# También podemos ver que la mayoría gasta más en alimentos, y se gasta menos en
# recreación.

boxplot(gasto2$Alimentos ~ gasto2$Ciudad, col = "lightgray")

vioplot(gasto2$Alimentos ~ gasto2$Ciudad, col = "lightgray")
# Vemos que en las 3 ciudades las gráficas son simétricas, por lo que no hay
# sesgo.
# También se puede apreciar que en la ciudad C se gasta más, y en la ciudad A se
# gasta menos.

# Gráficas de dispersión
# ======================
# Sirven para hacer correlaciones

pairs(gasto2[, -5], pch = 19)
# Vemos que educacin con recreación tiene una tendnecia lineal, a mayor gasto en
# educación, también mayor gasto en recreación.
# En alimentos y servicios no hay tendencia, los puntos están aleatorios.

pairs(gasto2[, -5], col = factor(gasto2$Ciudad), pch = 19)
# Vemos que hay correlación entre 2 ciudades, pero una no (la de puntos negros)

# Gráficas de correlación
# =======================

ggscatmat(gasto2[, -5])
# En la diagonal central se forma el gráfico de densidad para ver la
# distribución.
# A la izquierda vemos la tendencia
# A la derecha vemos la fuerza de correlación

ggpairs(gasto2, aes(colour = Ciudad))

# Calor
corrplot(cor(gasto2[, -5]), method = "shade", tl.col = "black")

# Círculos
corrplot(cor(gasto2[, -5]), method = "circle", tl.col = "black")
# Entre más grande sea el círculo, más fuerte es la correlación
# Entre más profundo el color, la correlación es positiva
# Entre menos profundo el color, la correlación es negativa

# Numéricos
corrplot(cor(gasto2[, -5]), method = "number", tl.col = "black")

# Numéricos y calor
corrplot.mixed(
  cor(gasto2[, -5]),
  lower = "number",
  upper = "shade",
  tl.col = "black"
)

# Gráficas de redes
# =======================
network_plot(corre, repel = FALSE)
# Matriz de correlación
# Entre más azul, mayor es la correlación, y viceversa

# Gráfico de coordenas paralelas
# ------------------------------
# 1:4 porque las columnas 1-4 son numéricas
# 5 es la columna con datos que no son numéricos, son las ciudades
ggparcoord(data = gasto2, columns = 1:4, groupColumn = 5, showPoints = FALSE)


# Gráficas de chernoff (caras)
# =============================
notas <- read_excel(
  path = "./Notas.xlsx",
  range = "B2:N27",
  col_names = TRUE
)

# Quitar la columna 1 del dataframe, pero guardarla como una etiqueta
# para las gráficas
# Quitamos la columna para no utilizarla en los cálculos, pero sí en las
# gráficas
df <- data.frame(notas[, -1], row.names = notas[, 1])

caras <- faces(df)

# Gráficas de estrellas
# =======================
stars(df, flip.labels = FALSE)
palette("Okabe-Ito")
stars(df, draw.segments = TRUE, flip.labels = FALSE, key.loc = c(15.5, 2.5))


# Transformación de datos
depar <- read.delim("clipboard")
depar1 <- data.frame(depar[, -1], row.names = depar[, 1])
boxplot(depar1)


#------------------------------------------------------------------#
#                  Corriengo el valor atípico                      #
#------------------------------------------------------------------#
depar2 <- depar1
hist(depar2$Inter)
depar2$Inter <- sqrt(depar2$Inter)
boxplot(depar2)

# Estandarización de variables
depar3 <- scale(depar2)
boxplot(depar3)
