library(forecast)
library(fpp2)
library(ggplot2)
library(TSA)
library(scales)
library(stats)
library(readxl)

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-2/")

ventas <- read_excel(
    path = "./data_series_tiempo.xlsx",
    range = "C1:C47",
    col_names = TRUE
)

yt <- ts(ventas, start = c(2019, 1), frequency = 12)
yt
plot(yt)

# Agrupacion Trimestres
boxplot(yt ~ cycle(yt))

## MÉTODO MULTIPLICATIVO
#  ===============================================
yt_desc <- decompose(yt, type = "multiplicative")
plot(yt_desc, xlab = "Años")
yt_desc

## Empezaremos a modelar con cada componente

df <- data.frame(Xt = seq(seq_along(yt)), yt, yt_desc$trend, yt_desc$seasonal)
names(df) <- c("Xt", "Yt", "tendencia", "Estacionalidad")

df
summary(df$tendencia)

# MIN : 90231
# MAX : 358858


library(sqldf)

df$tendencia_imput <- as.numeric(unlist(sqldf("
    select
    case
    when tendencia is null and Xt<=6  then 90231
    when tendencia is null and Xt>=41  then 358858
    else tendencia end tendencia_imput
    from df
")))
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
df

## Grafico del modelo final
# -------------------------------

plot(yt, col = "red")
points(df$Yt_est, type = "l", col = "blue")


## Predicción Para octubre del 2022
# -----------------------------------
predict(modelo_tendencia, data.frame(Xt = 53)) * df$Estacionalidad[5]

rmse <- sqrt(mean((yt - df$Yt_est)^2))
rmse
pema <- mean(abs(yt - df$Yt_est) / yt)
pema


## MÉTODO ADITIVO
#  ===============================================
yt_desc1 <- decompose(yt, type = "additive")
plot(yt_desc1, xlab = "Trimestre")
yt_desc1

## Empezaremos a modelar con cada componente

df1 <- data.frame(
    Xt = seq(seq_along(yt)), yt, yt_desc1$trend,
    yt_desc1$seasonal
)
names(df1) <- c("Xt", "Yt", "tendencia", "Estacionalidad")

summary(df1$tendencia)

# MIN : 90231
# MAX : 358858

library(sqldf)

df1$tendencia_imput1 <- as.numeric(unlist(sqldf("
select
case
when tendencia is null and Xt<=6  then 90231
when tendencia is null and Xt>=41  then 358858
else tendencia end tendencia_imput1
from df1")))


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
df1

## Grafico del modelo final
# -------------------------------

plot(yt, col = "red")
points(df1$Yt_est, type = "l", col = "blue")

predict(modelo_tendencia1, data.frame(Xt = 50)) + df1$Estacionalidad[2]

rmse <- sqrt(mean((yt - df1$Yt_est)^2))
rmse
pema <- mean(abs(yt - df1$Yt_est) / yt)
pema
