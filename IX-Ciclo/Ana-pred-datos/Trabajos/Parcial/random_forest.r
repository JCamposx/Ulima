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

library(caret)

set.seed(10)
train_index <- createDataPartition(data$price, p = 0.8, list = FALSE)
train <- data[train_index, ]
test <- data[-train_index, ]

# ===================================================================== #
# ======================= ESTIMACIÓN DEL MODELO ======================= #
# ===================================================================== #

# Creando los modelos
# ----------------------------
library(randomForest)

model <- randomForest(
  price ~ .,
  data = train,
  ntree = 500,
  mtry = 3
)

print(model)

predictions <- predict(model, newdata = test)
rmse <- sqrt(mean((test$price - predictions)^2))
mape <- mean(abs((test$price - predictions) / test$price))

print(paste("RMSE:", rmse))
print(paste("MAPE:", mape))
