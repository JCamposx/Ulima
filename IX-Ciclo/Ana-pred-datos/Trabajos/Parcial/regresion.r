# ===================================================================== #
# =============== ANÁLISIS DE REGRESIÓN MÚLTIPLE ====================== #
# ===================================================================== #

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Trabajos/Parcial")

data <- read.csv("./toyota.csv")

data$model <- trimws(data$model, "left")

data <- data[, !(names(data) %in% c())]

data <- na.omit(data)

summary(data)

data <- data[data$year >= 2016, ]

data$year <- as.factor(data$year)
data$model <- as.factor(data$model)
data$transmission <- as.factor(data$transmission)
data$fuelType <- as.factor(data$fuelType)

data$year

identify_outliers <- function(column) {
  if (!is.numeric(column) && !is.integer(column)) {
    return(rep(FALSE, length(column)))
  }

  q1 <- quantile(column, 0.25)
  q3 <- quantile(column, 0.75)
  iqr <- q3 - q1

  lower_limit <- q1 - 1.5 * iqr
  upper_limit <- q3 + 1.5 * iqr

  outliers <- column < lower_limit | column > upper_limit

  return(outliers)
}

outliers <- sapply(data, identify_outliers)

data <- data[!sapply(outliers, any), ]

data <- na.omit(data)

head(data)
str(data)
dim(data)

mean(data$price)

set.seed(10)
train_index <- createDataPartition(data$price, p = 0.8, list = FALSE)
train <- data[train_index, ]
test <- data[-train_index, ]

# ===================================================================== #
# ======================= ESTIMACIÓN DEL MODELO ======================= #
# ===================================================================== #

# Creando los modelos
# ----------------------------
model <- lm(
  price ~ .,
  data = train
)

summary(model)

# ================================================================== #
# ===================== SUPUESTO DE LINEALIDAD ===================== #
# ================================================================== #

library(ggplot2)
library(car)

# Gráfica de componente + residual
crPlots(model = model, terms = ~., )

# Gráfica de residuales vs valores individuale y generales / test
# ---------------------------------------------------------------
residualPlots(model, col.quad = "blue", smooth = TRUE, type = "rstudent")


# ===================================================================== #
# ====================== SUPUESTO DE NORMALIDAD ======================= #
# ===================================================================== #

# Gráfica normal y de densidad
# ----------------------------
ggplot(data = data, aes(resid(model))) +
  geom_density(adjust = 1) +
  lims(x = c(-30, 30)) +
  stat_function(fun = dnorm, args = list(
    mean = mean(model$residuals),
    sd = sd(model$residuals)
  ), color = "red")

# Gráfica QQPLOT
# --------------
qqnorm(model$residuals)
qqline(model$residuals)
## Residual normality
qqPlot(model)

# Prueba de normalidad
# --------------------
library(nortest)
shapiro.test(model$residuals)
ad.test(model$residuals)

# ===================================================================== #
# ===================== SUPUESTO DE HOCEDASTICIAD ===================== #
# ===================================================================== #

# Gráfica de residuales vs valore ajustados
# -----------------------------------------
ggplot(data = data, aes(model$fitted.values, model$residuals)) +
  geom_point() +
  geom_smooth(color = "firebrick", se = FALSE) +
  geom_hline(yintercept = 0) +
  theme_bw()

# Prueba de homocedasticidad
# ---------------------------
library(lmtest)
bptest(model)


# ===================================================================== #
# ===================== SUPUESTO DE INDEPENDENCIA ===================== #
# ===================================================================== #

# Gráfica de residuos vs la secuencia de orden
# --------------------------------------------
residuos <- model$residuals
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
dwt(model, alternative = "two.sided")


# ===================================================================== #
# ========================== MULTICOLINEALIDAD ======================== #
# ===================================================================== #

library(car)
vif(model)


# ===================================================================== #
# ====================== ESTIMACIÓN Y PREDICCIÓN ====================== #
# ===================================================================== #

# Estimar la tasa de predicción correcta en el modelo

predictions <- predict(model, newdata = test)

rmse <- sqrt(mean((test$price - predictions)^2))

mape <- mean(abs((test$price - predictions) / test$price))

print(paste("RMSE:", rmse))
print(paste("MAPE:", mape))
