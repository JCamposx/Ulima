library(forecast)
library(fpp2)
library(ggplot2)
library(TSA)
library(scales)
library(stats)
library(readxl)

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-2/")

operaciones <- read_excel(
    path = "./data_series_tiempo.xlsx",
    sheet = 2,
    range = "I1:I19",
    col_names = TRUE
)

# 2017 es el año en el que se empieza
# 1 es el mes, bimestre, trimestre o semestre en el que se empieza
# La frecuencia es la cantidad de tiempo de antes, meses sería 12 porque un
# año tiene 12 meses, pero como estamos trabajando con trimestres, es 4 proque
# un año tiene 4 trimestres
yt <- ts(operaciones, start = c(2017, 1), frequency = 4)
yt

plot(yt)

# Agrupacion Trimestres
boxplot(yt ~ cycle(yt))

## MÉTODO MULTIPLICATIVO
#  ===============================================
yt_desc <- decompose(yt, type = "multiplicative")

plot(yt_desc, xlab = "Trimestres")

yt_desc
yt_desc$trend

## Empezaremos a modelar con cada componente

df <- data.frame(
  Xt = seq(seq_along(yt)),
  yt,
  yt_desc$trend,
  yt_desc$seasonal
)

# Renombrar de las columnas
names(df) <- c("Xt", "Yt", "tendencia", "Estacionalidad")

df

summary(df$tendencia)

# MIN : 112.9
# MAX : 181.5


library(sqldf)

# Reemplazar los NAs con los valores máximos y mínimos del dataframe
df$tendencia_imput <- as.numeric(unlist(
  sqldf("
    select
    case
    when tendencia is null and Xt<=2  then 112.9
    when tendencia is null and Xt>=17  then 181.5
    else tendencia end tendencia_imput
    from df
  ")
))

df

# con lm relizamos un modelo de regresion lineal
# ==============================================

modelo_tendencia <- lm(tendencia_imput ~ Xt, data = df)
summary(modelo_tendencia)

# vamos a extraer los valores estimados
# -------------------------------------
df$tend_est <- modelo_tendencia$fitted.values

# al ser un modelo multiplicativo
# -------------------------------
df$Yt_est <- df$tend_est * df$Estacionalidad

## Grafico del modelo final
# -------------------------------

df

plot(yt, col = "red")
points(df$Yt_est, type = "l", col = "blue")


## Predicción para el 4to trimestre del 2021
# -----------------------------------
# Le ponemos 20 porque queremos predecir el trimestre 20. Si vemos el excel, lo
# que queremos predecir está en la fila 20 sin contar la cabecera
# Elegimos la estación 4 debido a que queremos precedir el trimestre #4
predict(modelo_tendencia, data.frame(Xt = 20)) * df$Estacionalidad[4]

# Raiz Media del Error Estandar
rmse <- sqrt(mean((yt - df$Yt_est)^2))
rmse

# Porcentaje de Error Medio Absoluto
pema <- mean(abs(yt - df$Yt_est) / yt)
pema

# ---------------------------------
# RESULTADOS MÉTODO MULTIPLICATIVO
# Predijo 195.0106
# RMSE 4.754686
# MAPE 0.0245119
# ---------------------------------


## MÉTODO ADITIVO
#  ===============================================
yt_desc1 <- decompose(yt, type = "additive")

plot(yt_desc1, xlab = "Trimestre")

yt_desc1

## Empezaremos a modelar con cada componente

df1 <- data.frame(
    Xt = seq(seq_along(yt)),
    yt,
    yt_desc1$trend,
    yt_desc1$seasonal
)

names(df1) <- c("Xt", "Yt", "tendencia", "Estacionalidad")

df1

summary(df1$tendencia)

# MIN : 112.9
# MAX : 181.5

library(sqldf)

df1$tendencia_imput1 <- as.numeric(unlist(
  sqldf("
    select
    case
    when tendencia is null and Xt<=2  then 112.9
    when tendencia is null and Xt>=17  then 181.5
    else tendencia end tendencia_imput1
    from df1
  ")
))

df1

# con lm relizamos un modelo de regresion lineal
# ==============================================

modelo_tendencia1 <- lm(tendencia_imput1 ~ Xt, data = df1)

summary(modelo_tendencia1)

# vamos a extraer los valores estimados
# -------------------------------------
df1$tend_est <- modelo_tendencia1$fitted.values

# al ser un modelo aditivo
# -------------------------------
df1$Yt_est <- df1$tend_est + df1$Estacionalidad

## Grafico del modelo final
# -------------------------------

df1

plot(yt, col = "red")
points(df1$Yt_est, type = "l", col = "blue")

predict(modelo_tendencia1, data.frame(Xt = 20)) + df1$Estacionalidad[4]

rmse <- sqrt(mean((yt - df1$Yt_est)^2))
rmse

pema <- mean(abs(yt - df1$Yt_est) / yt)
pema

# ---------------------------------
# RESULTADOS MÉTODO ADITIVO
# Predijo 2843
# RMSE 4.566022
# MAPE 0.02406653
# ---------------------------------

#=========================================================
# Por lo tanto, para este caso es mejor el método aditivo
# debido a que obtiene un menor RMSE y menor MAPE
#=========================================================
