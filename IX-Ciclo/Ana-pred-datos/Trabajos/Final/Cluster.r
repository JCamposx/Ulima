# ==================================================================#
#                          ANÁLISIS CLUSTER                        #
# ==================================================================#

#------------------------------------------------------------------#
#                          Librerías necesarias                    #
#------------------------------------------------------------------#

library(cluster)
library(factoextra)
library(ggplot2)
library(NbClust)
library(reshape2)
library(purrr)
library(ecodist)
library(clValid)

#------------------------------------------------------------------#
#              1.  Cluster no Jerarquico (Kmeas)                   #
#------------------------------------------------------------------#

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Trabajos/Final/")

datos <- read.csv(
  file = "./Datos/comportamiento_tc.csv"
)

datos <- na.omit(datos)

set.seed(123)

datos <- datos[sample(nrow(datos), 300), ]

View(datos)

drop_outliers <- function(
    data,
    threshold = 1.5,
    exclude_columns = c("TENURE")) {
  for (col in names(data)) {
    if (col %in% exclude_columns) {
      next
    }
    column_data <- data[[col]]
    q1 <- quantile(column_data, 0.25)
    q3 <- quantile(column_data, 0.75)
    iqr <- q3 - q1
    upper_fence <- q3 + threshold * iqr
    lower_fence <- q1 - threshold * iqr
    data <- data[!(column_data > upper_fence | column_data < lower_fence), ]
  }

  return(data)
}

datos <- drop_outliers(
  data = datos,
  exclude_columns = c("CUST_ID", "TENURE")
)

data <- data.frame(datos[, -1], row.names = datos$CUST_ID)

head(data)

str(data)

boxplot(data)

plot(density(data$BALANCE)) # Sesgo hacia la izquierda
data$BALANCE <- sqrt(data$BALANCE)
plot(density(data$BALANCE))

plot(density(data$BALANCE_FREQUENCY)) # Sesgo hacia la derecha
data$BALANCE_FREQUENCY <- data$BALANCE_FREQUENCY^2
plot(density(data$BALANCE_FREQUENCY))

plot(density(data$PURCHASES)) # Sesgo hacia la izquierda
data$PURCHASES <- sqrt(data$PURCHASES)
plot(density(data$PURCHASES))

plot(density(data$ONEOFF_PURCHASES)) # Sesgo hacia la izquierda
data$ONEOFF_PURCHASES <- sqrt(data$ONEOFF_PURCHASES)
plot(density(data$ONEOFF_PURCHASES))

plot(density(data$INSTALLMENTS_PURCHASES)) # Sesgo hacia la izquierda
data$INSTALLMENTS_PURCHASES <- log(data$INSTALLMENTS_PURCHASES)
plot(density(data$INSTALLMENTS_PURCHASES))

plot(density(data$CASH_ADVANCE)) # Sesgo hacia la izquierda
data$CASH_ADVANCE <- log(data$CASH_ADVANCE)
# data$CASH_ADVANCE <- data$CASH_ADVANCE ^ 2
plot(density(data$CASH_ADVANCE))

# --------------------------------------------------------------------------
plot(density(data$PURCHASES_FREQUENCY)) # Sesgo hacia la izquierda y derecha
# data$PURCHASES_FREQUENCY <- 1 / (data$PURCHASES_FREQUENCY)
# data$PURCHASES_FREQUENCY <- log(data$PURCHASES_FREQUENCY)
plot(density(data$PURCHASES_FREQUENCY))
# --------------------------------------------------------------------------

plot(density(data$ONEOFF_PURCHASES_FREQUENCY)) # Sesgo hacia la izquierda
# data$ONEOFF_PURCHASES_FREQUENCY <- log(data$ONEOFF_PURCHASES_FREQUENCY)
plot(density(data$ONEOFF_PURCHASES_FREQUENCY))

plot(density(data$PURCHASES_INSTALLMENTS_FREQUENCY)) # Sesgo hacia la izquierda
# data$PURCHASES_INSTALLMENTS_FREQUENCY <- log(
#   data$PURCHASES_INSTALLMENTS_FREQUENCY
# )
plot(density(data$PURCHASES_INSTALLMENTS_FREQUENCY))

plot(density(data$CASH_ADVANCE_FREQUENCY)) # Sesgo hacia la izquierda
# data$CASH_ADVANCE_FREQUENCY <- log(data$CASH_ADVANCE_FREQUENCY)
plot(density(data$CASH_ADVANCE_FREQUENCY))

plot(density(data$CASH_ADVANCE_TRX)) # Sesgo hacia la izquierda
# data$CASH_ADVANCE_TRX <- 1 / (data$CASH_ADVANCE_TRX)
plot(density(data$CASH_ADVANCE_TRX))

plot(density(data$PURCHASES_TRX)) # Sesgo hacia la izquierda
data$PURCHASES_TRX <- log(data$PURCHASES_TRX)
plot(density(data$PURCHASES_TRX))

plot(density(data$CREDIT_LIMIT)) # Sesgo hacia la izquierda
# data$CREDIT_LIMIT <- log(data$CREDIT_LIMIT)
plot(density(data$CREDIT_LIMIT))

plot(density(data$PAYMENTS)) # Sesgo hacia la izquierda
data$PAYMENTS <- log(data$PAYMENTS)
plot(density(data$PAYMENTS))

plot(density(data$MINIMUM_PAYMENTS)) # Sesgo hacia la izquierda
data$MINIMUM_PAYMENTS <- log(data$MINIMUM_PAYMENTS)
plot(density(data$MINIMUM_PAYMENTS))

plot(density(data$PRC_FULL_PAYMENT)) # Sesgo hacia la izquierda
data$PRC_FULL_PAYMENT <- 1 / (data$PRC_FULL_PAYMENT)
plot(density(data$PRC_FULL_PAYMENT))

plot(density(data$TENURE)) # Sesgo hacia la derecha
# data$TENURE <- data$TENURE ^ 2
plot(density(data$TENURE))

boxplot(data)

#-------------------------------------------------------------------#
#                           Realizando PCA                          #
#-------------------------------------------------------------------#

library(FactoMineR)
library(factoextra)
library(ggpubr)
library(PerformanceAnalytics)
library(corrplot)
library(rgl)

# Con scale se estandarizan la unidad de las variables
res_pca <- PCA(scale(data), graph = FALSE)

# Autovalores y porcentaje de varianza explicada
eig_val <- get_eigenvalue(res_pca)
eig_val

# Grabar los datos y los resultados de los scores en un archivo CSV
salida_acp <- res_pca$ind$coord[, 1:5]
salida_acp

data_final <- data.frame(CUST_ID = datos$CUST_ID, salida_acp)

View(data_final)

#-------------------------------------------------------------------#
#                    Estandarizando las variables                   #
#-------------------------------------------------------------------#

df <- data.frame(data_final[, -1], row.names = data_final$CUST_ID)

View(df)

boxplot(df)

#------------------------------------------------------------------#
#                       Estandarizando las variables               #
#------------------------------------------------------------------#

#= df <- scale(data)

#= boxplot(df)

#------------------------------------------------------------------#
#              Analizando las correlaciones                        #
#------------------------------------------------------------------#

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

#------------------------------------------------------------------#
#                          ALGORITMO K-MEAN                        #
#------------------------------------------------------------------#
#------------------------------------------------------------------#
#                  Eligiendo el número de Cluster                  #
#------------------------------------------------------------------#

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

#------------------------------------------------------------------#
#                     Visualización de los cluster                 #
#------------------------------------------------------------------#

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

#------------------------------------------------------------------#
#                 Características de los cluster                   #
#------------------------------------------------------------------#

# Cortando en 3 cluster
# ---------------------
grp3 <- km_res$cluster

grp3

# Number de casos en cada cluster
# -------------------------------
table(grp3)

data.frame(datos, grp3)

# Descripción de cada cluster
# ---------------------------
med3 <- aggregate(data, by = list(cluster = grp3), mean)

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

clust_car(data, grp3, standarized = FALSE)

# Diagrama de caracterización 2
# -------------------------------
dd <- cbind(data, cluster = grp3)

dd$cluster <- as.factor(dd$cluster)

df_m <- reshape2::melt(dd, id.var = "cluster")

p <- ggplot(data = df_m, aes(x = variable, y = value)) +
  geom_boxplot(aes(fill = cluster)) +
  facet_wrap(~variable, scales = "free")

p

data.frame(datos, grp3)
