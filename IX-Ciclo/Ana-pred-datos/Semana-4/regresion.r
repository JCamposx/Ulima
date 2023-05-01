# ===================================================================== #
# =============== ANÁLISIS DE REGRESIÓN MÚLTIPLE ====================== #
# ===================================================================== #
library(readxl)

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-4/")

data <- read_excel(
  path = "./PrecioVivienda.xlsx",
  sheet = 1,
  range = "A1:G129",
  col_names = TRUE
)

data


# ===================================================================== #
# ======================= ESTIMACIÓN DEL MODELO ======================= #
# ===================================================================== #

# Creando los modelos
# ----------------------------
# Variable dependiente: Precio
# Variables independientes: Piescuad, Cuartos, Baños, Ofertas, Ladrillo, Barrio

modelo_A <- lm(
  Precio ~ Piescuad + Cuartos + Baños + Ofertas + Ladrillo + Barrio,
  data = data
)
summary(modelo_A)

modelo_B <- lm(Precio ~ ., data = data)
summary(modelo_B)

# Vemos que en Coefficients nos retorna esto:
#
# Coefficients:
#              Estimate Std. Error t value Pr(>|t|)
# (Intercept)   598.919   9552.197   0.063  0.95011
# Piescuad       52.994      5.734   9.242 1.10e-15 ***
# Cuartos      4246.794   1597.911   2.658  0.00894 **
# Baños        7883.278   2117.035   3.724  0.00030 ***
# Ofertas     -8267.488   1084.777  -7.621 6.47e-12 ***
# LadrilloSi  17297.350   1981.616   8.729 1.78e-14 ***
# BarrioNorte  1560.579   2396.765   0.651  0.51621
# BarrioOeste 22241.616   2531.758   8.785 1.32e-14 ***
#
# De esta tabla, Estimate son los coeficientes de la fórmula de regresión (es
# decir, son beta0, beta1, beta2, beta3, ...).
#
# Creación del modelo
# -------------------
# Precio = 598.919 + 52.994*Piescuad + 4246.794*Cuartos + 7883.278*Baños
#          - 8267.488*Ofertas + 17297.350*LadrilloSi + 1560.579*BarrioNorte
#          + 22241.616*BarrioOeste
#
# Interpretación de coeficientes
# ------------------------------
# Para una variable cuantitativa
# beta2 = 4246.794: Cuando una cada tiene un cuarto adicional, el precio
#                   aumenta en 4246.794 dólares, manteniendo constantes las
#                   demás variables.
#
# Para una variable cualitativa
# beta5 = 17297.350: Cuando la casa es de ladrillo, el precio aumenta en
#                    17297.350 dólares, manteniendo constantes las demás
#                    variables.
# beta6 = 1560.579: Cuando la casa está en el barrio norte, el precio aumenta en
#                   1560.579 dólares, manteniendo constantes las demás
#                   variables.
#
# Prueba de hipótesis global
# --------------------------
# H0: B1 = B2 = B3 = B4 = B5 = B6 = B7 = 0 (el modelo no es válido)
# H1: Al menos un beta Bi es diferente de 0, por lo que sí existe regresión
#     lineal, el modelo es válido.
# alpha = 0.05
#
# Esto lo obtenemos en la última línea que nos retornó el summary
# F-statistic: 113.3, p-value: < 2.2e-16
#
# Como p-value < alpha, se rechaza H0
# Por lo tanto, a un nivel de significancia del 0.05, existe suficiente
# evidencia estadística para afirmar que al menos un beta Bi es diferente de
# cero.
#
# Prueba de hipótesis individual
# ------------------------------
# Para esto, tenemos que revisar lo obtenido en Coefficients, las columnas
# t-value y Pr(>|t|)
# OJO: Si se divide un beta entre su error estándar, obtenemos el t-value
#
# Por ejemplo, para beta B1:
# HO: B1 = 0
# H1: B1 != 0
# alpha = 0.05
# t-value = 9.242
# p-value = 1.10e-15
#
# Como p-value < alpha, se rechaza H0.
# Por lo tanto, a un nivel de significancia del 0.05, existe sufienciente
# evidencia estadística para afirmar que B1 es diferente de cero. Esto indica
# que los la variable Piescuad sí influye en el modelo.
#
# R nos ayuda en esto con las estrellas que aparecen al final de cada fila en la
# tabla de Coefficients. Así podemos ver que BarrioNorte no es una variable
# significativa ya que no tiene estrellas debido a que su p-value es mayor a
# 0.05 (alpha).
#
# Lo que se debe hacer es eliminar estas variables ya que no son significativas.
# Al borrarlas, hay que volver a correr el summary ya que hace que todo lo que
# se obtuvo antes cambie.
#
# Sin embargo, en este caso específico, no se puede borrar la variable
# BarrioNorte, ya que no es una variable completa como tal, sino que es un nivel
# de la variable Barrio. En casos como este, basta que un solo nivel sea
# significativo para que toda la variable entera sea significativa.
#
# Bondad de ajuste
# ----------------
# Esto lo obtenemos del summary
# Multiple R-squared: 0.8686, Adjusted R-squared: 0.861
#
# Como estamos en un caso de regresión múltiple (count(variables) >= 2), debemos
# trabajar con el r-cuadrado ajustado.
#
# Por lo tanto, el 86.10 % de la variabilidad del precio es explicado por las
# variables a través del modelo de regresión.
# En otras palabras, las variables del modelo influyen en un 86.10% en el
# precio de las casas, y el 13.90% restante se debe a otras variables que no
# están incluidas en el modelo.
# Por lo tanto, el modelo es bueno ya que el r-cuadrado ajustado > 80%.
#
# Residual standard error: 10020 (EER)
# El EER sirve para determinar la capacidad predictiva del modelo.
# Se observa un EER de 10020. Este debe compararse con otro modelo para ver cuál
# es mejor.


# ================================================================== #
# ===================== SUPUESTO DE LINEALIDAD ===================== #
# ================================================================== #

library(ggplot2)
library(car)

# Gráfica de componente + residual
crPlots(model = modelo_B, terms = ~., )

# Gráfica de residuales vs valores individuale y generales / test
# ---------------------------------------------------------------
residualPlots(modelo_B, col.quad = "blue", smooth = TRUE, type = "rstudent")


# ===================================================================== #
# ====================== SUPUESTO DE NORMALIDAD ======================= #
# ===================================================================== #

# Gráfica normal y de densidad
# ----------------------------
ggplot(data = data, aes(resid(modelo_B))) +
  geom_density(adjust = 1) +
  lims(x = c(-30, 30)) +
  stat_function(fun = dnorm, args = list(
    mean = mean(modelo_B$residuals),
    sd = sd(modelo_B$residuals)
  ), color = "red")

# Gráfica QQPLOT
# --------------
qqnorm(modelo_B$residuals)
qqline(modelo_B$residuals)
## Residual normality
qqPlot(modelo_B)

# Prueba de normalidad
# --------------------
library(nortest)
shapiro.test(modelo_B$residuals)
ad.test(modelo_B$residuals)

# OJO
# Si count(datos) < 50, usar Shapiro
# Sino, usar Anderson-Darling


# ===================================================================== #
# ===================== SUPUESTO DE HOCEDASTICIAD ===================== #
# ===================================================================== #

# Gráfica de residuales vs valore ajustados
# -----------------------------------------
ggplot(data = data, aes(modelo_B$fitted.values, modelo_B$residuals)) +
  geom_point() +
  geom_smooth(color = "firebrick", se = FALSE) +
  geom_hline(yintercept = 0) +
  theme_bw()

# Prueba de homocedasticidad
# ---------------------------
library(lmtest)
bptest(modelo_B)


# ===================================================================== #
# ===================== SUPUESTO DE INDEPENDENCIA ===================== #
# ===================================================================== #

# Gráfica de residuos vs la secuencia de orden
# --------------------------------------------
residuos <- modelo_B$residuals
ggplot(data = data, aes(x = seq_along(residuos), y = residuos)) +
  geom_point(aes(color = residuos)) +
  scale_color_gradient2(low = "blue3", mid = "grey", high = "red") +
  geom_line(size = 0.3) +
  labs(title = "Distribución de los residuos", x = "index", y = "residuo") +
  geom_hline(yintercept = 0) +
  theme_bw() +
  theme(plot.title = element_text(hjust = 0.5), legend.position = "none")

library(car)
set.seed(100)
dwt(modelo_B, alternative = "two.sided")


# ===================================================================== #
# ========================== MULTICOLINEALIDAD ======================== #
# ===================================================================== #

library(car)
vif(modelo_B)


# ===================================================================== #
# ====================== ESTIMACIÓN Y PREDICCIÓN ====================== #
# ===================================================================== #

# Estimar la tasa de predicción correcta en el modelo

Yt <- data$Precio
Yt_est <- as.numeric(modelo_B$fitted.values)

RMSE <- sqrt(mean((Yt - Yt_est)^2))
RMSE
PEMA <- mean(abs(Yt - Yt_est) / Yt)
PEMA

predict(modelo_B, data.frame(
  Piescuad = 2000,
  Cuartos = 6,
  Baños = 4,
  Ofertas = 2,
  Ladrillo = "Si",
  Barrio = "Este"
))
