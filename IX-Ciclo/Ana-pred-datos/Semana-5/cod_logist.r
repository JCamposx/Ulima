# ============================================================#
#                REGRESIÓN LOGÍSTICA BINARIA                 #
# ============================================================#

library(readxl)

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-5/")

df <- read_excel(
  path = "./BASE.xlsx",
  sheet = 1,
  range = "A1:G108",
  col_names = TRUE
)

# De los datos, seleccionamos la variable dependiente, la que se intenta
# predecir, y lo convertimos en factor. Esto asegura que esta variable sea una
# variable categórica.
df$Seguro <- as.factor(df$Seguro)

head(df)

str(df)



#------------------------------------------------------------#
#                     Elección del modelo                    #
#------------------------------------------------------------#
model <- glm( # gml = modelo lineal generalizado
  Seguro ~ Sexo + Edad + Hijos + Ingresos + Empresa + Automóvil,
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
gl
pvalue

# Selección de variables
library(MASS)

step <- stepAIC(model, direction = "backward", trace = FALSE)

step$anova # Retorna el modelo final recomendado que tiene menor AIC

# AIC es un criterio de información. Sirve para comparar modelos. Un modelo es
# mejor cuando tiene menor AIC. Sin embargo, el hecho de que uno sea mejor que
# otro no indica que el modelo sea bueno.

model_2 <- glm( # Este es el modelo recomendado por AIC
  Seguro ~ Edad + Hijos + Empresa + Automóvil,
  family = binomial(link = logit),
  data = df
)

summary(model_2)

# Prueba global
# -------------

chi2 <- model_2$null.deviance - model_2$deviance
gl <- model_2$df.null - model_2$df.residual
pvalue <- 1 - pchisq(chi2, gl)

chi2
gl
pvalue

# Coeficiente de ventajas (OR) e IC 95%
# -------------------------------------
exp(cbind(OR = coef(model_2), confint.default(model_2)))

# +-------------+-------------+--------------+-------------+
# |             |          OR |        2.5 % |      97.5 % |
# +-------------+-------------+--------------+-------------+
# | (Intercept) | 0.001027764 | 0.0000357957 |  0.02950908 |
# | Edad        | 1.134245216 | 1.0686662831 |  1.20384841 |
# | Hijos       | 1.523233451 | 1.0019148097 |  2.31580582 |
# | Empresa     | 0.989702729 | 0.9819374823 |  0.99752938 |
# | AutomóvilSí | 6.752402432 | 1.9486635142 | 23.39805629 |
# +-------------+-------------+--------------+-------------+
#
# Interpretación
# AutomóvilSí = 6.752402432, Cuando una persona tiene automóvil, tiene 6.75
#                            veces más posibilidades de adquirir un seguro.
# Hijos = 1.523233451, Cuando una persona tiene hijos, tiene 1.52 veces más
#                      posibilidades de adquirir un seguro.

#------------------------------------------------------------#
#   Prueba de Hosmer y Lemeshow para adecuación del modelo   #
#------------------------------------------------------------#
library(generalhoslem)

logitgof(df$Seguro, fitted(model_2))

# H0: El modelo ajustado es adecuado
# H1: El modelo ajustado no es adecuado
#
# X-squared = 7.7582, df = 8, p-value = 0.4574
#
# Como el p-value es mayor que alfa (0.05), no se rechaza H0. Por lo tanto, el
# modelo ajustado es adecuado.

#------------------------------------------------------------#
#    R2 - medidas de bondad de ajuste y prueba del modelo    #
#------------------------------------------------------------#
library(rcompanion)

nagelkerke(model_2)

# +------------------------------+------------------+
# |                              | Pseudo.R.squared |
# +------------------------------+------------------+
# | McFadden                     |         0.373471 |
# | Cox and Snell (ML)           |         0.400832 |
# | Nagelkerke (Cragg and Uhler) |         0.537112 |
# +------------------------------+------------------+
#
# Los pseudo R2 son altos. Muestran la variabilidad explicada por el modelo. Por
# ejemplo, el de Nagelkerke estima en un 53.71% tal variabilidad.

#------------------------------------------------------------#
#              Probabilidades y grupo estimadas              #
#------------------------------------------------------------#
library(ggplot2)

# Hacemos las predicciones
proba_pred <- predict(model_2, type = "response")

proba_pred

# Si la probabilidad es mayor a 0.5, la persona sí va a adquirir el seguro
clase_pred <- ifelse(proba_pred >= 0.5, "Sí", "No")

clase_pred

# Creamos un dataframe con las predicciones
final_data <- cbind(df, proba_pred, clase_pred)

final_data

ggplot(final_data, aes(x = proba_pred, fill = Seguro)) +
  geom_density(alpha = 0.5)

# En el plot, vemos que hay un cruce entre azul y rojo. Este cruce es el error,
# los valores que son azules pero están clasificados como rojos, y viceversa.

#------------------------------------------------------------#
#                   Tabla de clasificación                   #
#------------------------------------------------------------#
# Calcular el accuracy
# --------------------
accuracy <- mean(df$Seguro == clase_pred)

accuracy

# Calcular el error de mala clasificación
# ---------------------------------------
error <- mean(df$Seguro != clase_pred)

error

# Matriz de confusión
# -------------------
library(caret)

confusionMatrix(as.factor(clase_pred), df$Seguro, positive = "Sí")

#
#           Reference
# Prediction No Sí
#         No 32 10
#         Sí 15 50
#
#                Accuracy : 0.7664 # ES EL ACCURACY
#                  95% CI : (0.6747, 0.8427)
#     No Information Rate : 0.5607
#     P-Value [Acc > NIR] : 7.776e-06
#
#                   Kappa : 0.5202
#
#  Mcnemar's Test P-Value : 0.4237
#
#             Sensitivity : 0.8333 # ES LA SENSIBILIDAD
#             Specificity : 0.6809 # ES LA ESPECIFICIDAD
#          Pos Pred Value : 0.7692
#          Neg Pred Value : 0.7619
#              Prevalence : 0.5607
#          Detection Rate : 0.4673
#    Detection Prevalence : 0.6075
#       Balanced Accuracy : 0.7571
#
#        'Positive' Class : Sí
#
# Accuracy = 0.7664: El modelo predice correctamente en un 76.64%.
#
# Error = 0.2336: El modelo se equivoca en un 23.36%. Se calcula con 1 - Acc.
#
# Sensibilidad = 0.8333: El modelo predice correctamente a los que sí van a
#                         adquirir el seguro en un 83.33%.
#
# Especificidad = 0.8333: El modelo predice correctamente a los que no van a
#                          adquirir el seguro en un 68.09%.


# Curva Roc
# ---------
library(ROSE)

roc.curve(
  df$Seguro,
  proba_pred,
  lty = 2,
  lwd = 1.8,
  col = "blue",
  main = "ROC curves"
)

# Area under the curve (AUC): 0.881
#
# Existe un 88.10% de probabilidad de que el diagnóstico realizado a un cliente
# que adquiere el seguro sea más correcto que el de un cliente que no adquiere
# el seguro.

#------------------------------------------------------------#
#                     Prediciendo datos                      #
#------------------------------------------------------------#
# Estimado en forma particular
# ----------------------------
p <- predict(
  model_2,
  data.frame(
    Edad = 44,
    Hijos = 3,
    Empresa = 90,
    Automóvil = "Sí"
  ),
  type = "response"
)

p # 0.7115742

# Para ecuación
# -------------
log_odds <- predict(
  model_2,
  data.frame(
    Edad = 44,
    Hijos = 3,
    Empresa = 90,
    Automóvil = "Sí"
  ),
  type = "link"
)

exp(log_odds) / (1 + exp(log_odds)) # 0.7115742

odds <- p / (1 - p)

odds # 2.467096

exp(log_odds) # 2.467096
