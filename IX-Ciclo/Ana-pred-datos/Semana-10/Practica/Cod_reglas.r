##########################################################
#                   Ejemplo: Cosmetico                   #
##########################################################

#--------------------------------------------------------#
#           Paso 1: Obtener y procesar la data           #
#--------------------------------------------------------#

# Usando librería arules
library(arules)
library(ggraph)
library(arulesViz)
library(TSP)

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-10/Practica")

cosmetico <- read.transactions(
  "./Cosmetic.csv",
  header = FALSE,
  format = "basket",
  sep = ","
)

View(cosmetico)

# Ver la data sparse
cosmetico@data

# Visualizar la matriz de transacciones
# (p. ej. para las 5 primeras transacciones)
image(cosmetico[1:5])

# Visualizar la matriz de transacciones
# (p. ej. seleccionar al azar 100 transacciones)
set.seed(180)
image(sample(cosmetico, 100))

# Ver un resumen
summary(cosmetico)

# Mostrar las transacciones
labels(cosmetico)

# Mostrar un subconjunto de transacciones (p. ej. las cinco primeras)
inspect(cosmetico[1:5])

quantile(size(cosmetico), probs = seq(0, 1, 0.1))

# Mostrar el soporte (proporción de transacciones) de un ítem
# (p. ej. de los 14 primeros)
itemFrequency(cosmetico[, 1:14])

# Visualizar el soporte de los ítems
# (p. ej. de aquellos items con una proporción mayor a 0.50)
itemFrequencyPlot(cosmetico, support = 0.50)

# Visualizar el soporte de los ítems
# (p. ej.de los 14 ítems con mayor soporte)
itemFrequencyPlot(cosmetico, topN = 14)

#--------------------------------------------------------#
#        Paso 2: Entrenar el modelo con los datos        #
#--------------------------------------------------------#

help(apriori)

apriori(cosmetico)

cosmetico_rules <- apriori(
  cosmetico,
  parameter = list(support = 0.1, confidence = 0.5, minlen = 2)
)

cosmetico_rules

# Visualización
# --------------
library(arulesViz)
plot(cosmetico_rules)
plot(cosmetico_rules, method = "graph", control = list(alpha = 1))

subrules <- head(sort(cosmetico_rules, by = "lift"), 10)
plot(subrules)
plot(subrules, method = "graph", control = list(alpha = 1))
plot(subrules, method = "paracoord")

#--------------------------------------------------------#
#                Paso 3: Evaluar el modelo               #
#--------------------------------------------------------#

summary(cosmetico_rules)

# Mostrar las tres primeras reglas de asociación
inspect(cosmetico_rules[1:3])

# Mostrar las 5 reglas con mayor lift
inspect(sort(cosmetico_rules, by = "lift")[1:5])

# Subconjuntos de reglas
bayasrules <- subset(cosmetico_rules, items %in% "bayas")
inspect(bayasrules)

bayas_yogurtrules <- subset(cosmetico_rules, items %in% c("bayas", "yogurt"))
inspect(bayas_yogurtrules)

bayasrules <- subset(cosmetico_rules, items %ain% c("bayas", "yogurt"))
inspect(bayasrules)

# Para obtener solo galletas en el consecuente (Y)
reglas_mascara_1 <- apriori(
  cosmetico,
  parameter = list(support = 0.1, confidence = 0.5, minlen = 2),
  appearance = list(rhs = "Mascara")
)

inspect(sort(reglas_mascara_1, by = "lift")[1:5])

# Para obtener solo galletas en el antecedente (X)
reglas_mascara_2 <- apriori(
  cosmetico,
  parameter = list(support = 0.1, confidence = 0.5, minlen = 2),
  appearance = list(lhs = "Mascara")
)

inspect(sort(reglas_mascara_2, by = "lift"))

# Exportar las reglas obtenidas
if (!dir.exists("./resultados/")) dir.create("./resultados/")

write(
  cosmetico_rules,
  file = "./resultados/supermercadorules.csv",
  sep = ",",
  quote = TRUE,
  row.names = FALSE
)

# Convertir reglas en dataframe
supermercadorules_df <- as(cosmetico_rules, "data.frame")
head(supermercadorules_df)
