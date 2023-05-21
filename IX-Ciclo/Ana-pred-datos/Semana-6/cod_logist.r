# ============================================================#
#                REGRESIÓN LOGÍSTICA BINARIA                  #
# ============================================================#

library(readxl)

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-6/")

df <- read_excel(
  path = "./Cupones.xlsx",
  sheet = 1,
  range = "A1:D101",
  col_names = TRUE
)

# De los datos, seleccionamos la variable dependiente, la que se intenta
# predecir, y lo convertimos en factor. Esto asegura que esta variable sea una
# variable categórica.
df$Cupon <- as.factor(df$Cupon)

# Es necesario hacer lo mismo con Tarjeta ya que como está en 0 y 1, R va a
# entender que el 1 es mayor que 0, no que 0 es no y 1 es sí.
df$Tarjeta <- as.factor(df$Tarjeta)

head(df)

str(df)

#------------------------------------------------------------#
#                     Elección del modelo                    #
#------------------------------------------------------------#
model <- glm( # gml = modelo lineal generalizado
  Cupon ~ Gasto + Tarjeta,
  family = binomial(link = logit),
  data = df
)

summary(model)

# Prueba global
# -------------

chi2 <- model$null.deviance - model$deviance
gl <- model$df.null - model$df.residual
pvalue <- 1 - pchisq(chi2, gl)

chi2
pvalue

# Prueba global
# -------------

chi2 <- model$null.deviance - model$deviance
gl <- model$df.null - model$df.residual
pvalue <- 1 - pchisq(chi2, gl)

chi2
pvalue

# Coeficiente de ventajas (OR) e IC 95%
# -------------------------------------
library(MASS)
exp(cbind(OR = coef(model), confint.default(model)))

#------------------------------------------------------------#
#   Prueba de Hosmer y Lemeshow para adecuación del modelo   #
#------------------------------------------------------------#
library(generalhoslem)

logitgof(df$Cupon, fitted(model))

#------------------------------------------------------------#
#    R2 - medidas de bondad de ajuste y prueba del modelo    #
#------------------------------------------------------------#
library(rcompanion)

nagelkerke(model)

#------------------------------------------------------------#
#              Probabilidades y grupo estimadas              #
#------------------------------------------------------------#
library(ggplot2)

# Hacemos las predicciones
proba_pred <- predict(model, type = "response")
proba_pred

# Si la probabilidad es mayor a 0.5, la persona sí va a adquirir el seguro
clase_pred <- ifelse(proba_pred >= 0.5, "Si", "No")
clase_pred

# Creamos un dataframe con las predicciones
final_data <- cbind(df, proba_pred, clase_pred)
final_data

ggplot(final_data, aes(x = proba_pred, fill = Cupon)) +
  geom_density(alpha = 0.5)

#------------------------------------------------------------#
#                   Tabla de clasificación                   #
#------------------------------------------------------------#
# Calcular el accuracy
# --------------------
accuracy <- mean(df$Cupon == clase_pred)
accuracy

# Calcular el error de mala clasificación
# ---------------------------------------
error <- mean(df$Cupon != clase_pred)
error

# Matriz de confusión
# -------------------
library(caret)

confusionMatrix(as.factor(clase_pred), df$Cupon, positive = "Si")

# Curva Roc
# ---------
library(ROSE)

roc.curve(
  df$Cupon,
  proba_pred,
  lty = 2,
  lwd = 1.8,
  col = "blue",
  main = "ROC curves"
)

#------------------------------------------------------------#
#                     Prediciendo datos                      #
#------------------------------------------------------------#
# Estimado en forma particular
# ----------------------------
p <- predict(
  model,
  data.frame(
    Gasto = 5,
    Tarjeta = "1"
  ),
  type = "response"
)

p
