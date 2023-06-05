#===================================================================#
#                          ANÁLISIS CLUSTER                         #
#===================================================================#

#-------------------------------------------------------------------#
#                        Librerías necesarias                       #
#-------------------------------------------------------------------#

library(cluster)
library(factoextra)
library(ggplot2)
library(NbClust)
library(reshape2)
library(purrr)
library(ecodist)
library(clValid)
library(readxl)

#-------------------------------------------------------------------#
#                  1. Cluster no Jerarquico (Kmeas)                 #
#-------------------------------------------------------------------#

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-9/Practica/")

data <- read_excel(
  path = "./Violencia_USA.xlsx",
  sheet = 1,
  range = "A1:E51",
  col_names = TRUE
)

head(data)

data_1 <- data.frame(data[, -1], row.names = data$Estados)

boxplot(data_1) # Vemos que Violación tiene outliers

plot(density(data_1$Violación)) # Vemos que hay sesgo hacia la izquierda

data_1$Violación <- sqrt(data_1$Violación) # Estandarizamos Violación
                                           # (Raíz porque es sesgo hacia iz)

boxplot(data_1)

#-------------------------------------------------------------------#
#                    Estandarizando las variables                   #
#-------------------------------------------------------------------#

df <- scale(data_1)

boxplot(df)

#-------------------------------------------------------------------#
#                    Analizando las correlaciones                   #
#-------------------------------------------------------------------#

cor(df)

library(corrplot)

# Numéricos y círculos
corrplot.mixed(cor(df),
  lower = "number",
  upper = "circle",
  tl.col = "black"
)

library(GGally)

ggscatmat(df, corMethod = "pearson") + theme_bw()

# Vemos que hay una alta correlación (0.8) entre Asalto y Asesinato

#-------------------------------------------------------------------#
#               Distancia/Similaridad entre resgistros              #
#-------------------------------------------------------------------#

dist(df)

#-------------------------------------------------------------------#
#                          ALGORITMO K-MEAN                         #
#-------------------------------------------------------------------#
#-------------------------------------------------------------------#
#                   Eligiendo el número de Cluster                  #
#-------------------------------------------------------------------#

n_clus <- NbClust(
  df,
  distance = "euclidean",
  min.nc = 3,
  max.nc = 8,
  method = "kmeans",
  index = "alllong"
) # Vemos que nos recomiendan 3 clusters

n_clus

par(mfrow = c(1, 1))

res <- data.frame(t(n_clus$Best.nc))

table(res$Number_clusters)

barplot(table(res$Number_clusters), col = "blue")

#-------------------------------------------------------------------#
#                    Visualización de los cluster                   #
#-------------------------------------------------------------------#

set.seed(100)

# Kmeans con 3 clusters porque antes se nos recomendó 3 clusters
km_res <- kmeans(df, 3, nstart = 25)

fviz_cluster(
  km_res,
  data = df,
  palette = "jco",
  ellipse.type = "euclid", # Concentration ellipse
  star.plot = TRUE, # Add segments from centroids to items
  repel = TRUE, # Avoid label overplotting (slow)
  ggtheme = theme_minimal()
)

fviz_cluster(
  km_res,
  data = df,
  palette = "jco",
  ellipse.type = "convex", # Concentration ellipse
  star.plot = TRUE, # Add segments from centroids to items
  repel = TRUE, # Avoid label overplotting (slow)
  ggtheme = theme_minimal()
)

#-------------------------------------------------------------------#
#                   Características de los cluster                  #
#-------------------------------------------------------------------#

# Cortando en 3 cluster
# ---------------------
grp3 <- km_res$cluster

grp3

# Number de casos en cada cluster
# -------------------------------
table(grp3)

data.frame(data, grp3)

# Descripción de cada cluster
# ---------------------------
med3 <- aggregate(data_1, by = list(cluster = grp3), mean)

med3

# Diagrama de caracterización
# ---------------------------
clust_car <- function(data, cluster, standarized) {
  data1 <- data

  if (standarized) data1 <- scale(data1)

  m <- as.data.frame(
    t(rbind(aggregate(data1, by = list(cluster = cluster), mean)[, -1]))
  )

  a <- as.vector(colMeans(data1))

  fin <- data.frame(m, a, names(data))

  names(fin) <- c(paste0("clus", 1:nlevels(as.factor(cluster))), "Media", "var")

  ali <- reshape2::melt(fin, id.vars = "var")

  ggplot(
    ali, aes(x = var, y = round(value, 1), group = variable, colour = variable)
  ) +
    geom_point() +
    geom_line(aes(lty = variable)) +
    # expand_limits(y = c(-1.9, 1.9)) +
    labs(y = "value")
}

clust_car(data_1, grp3, standarized = FALSE)

# Diagrama de caracterización 2
# -----------------------------
dd <- cbind(data_1, cluster = grp3)

dd$cluster <- as.factor(dd$cluster)

df_m <- reshape2::melt(dd, id.var = "cluster")

p <- ggplot(data = df_m, aes(x = variable, y = value)) +
  geom_boxplot(aes(fill = cluster)) +
  facet_wrap(~variable, scales = "free")

p
