library(FactoMineR)
library(factoextra)
library(ggpubr)
library(PerformanceAnalytics)
library(corrplot)
library(rgl)
library(readxl)

setwd("/workspaces/Ulima/IX-Ciclo/Ana-pred-datos/Semana-7/")

datos_acp <- read_excel(
  path = "./Distritos.xlsx",
  sheet = 1,
  range = "B2:S85",
  col_names = TRUE
)
head(datos_acp)

# Para hacer PCA, se necesitan solo datos cuantitavos. En los datos que se
# tienen, estos están a aprtir de la columna 4
datos_acp1 <- datos_acp[, 4:18]
head(datos_acp1)

# =================================================================#
#                     Análisis de Correlación                     #
# =================================================================#

# Matriz de Variancia-Covariancia
#---------------------------------
# Para que los números no aparezcan en notación científica
options(scipen = 999)

# Calculamos las covarianzas
cov(datos_acp1)
# Las varianzas están en la diagonal principal. El resto son covarianzas.

# Obtenemos la diagonal, es decir, las varianzas.
diag(cov(datos_acp1))

# Coeficientes de Correlación
#-----------------------------
round(cor(datos_acp1), 3)

chart.Correlation(datos_acp1, histogram = TRUE, pch = 20)

corrplot.mixed(cor(datos_acp1),
  lower = "number",
  upper = "shade",
  tl.col = "black"
)


# =================================================================#
#               Análisis de Componentes Principales               #
# =================================================================#

#-----------------------------------------------------------------#
#                 Hallando la matriz de distancias                #
#-----------------------------------------------------------------#
# Formando los componentes principales
# Con scale se estandarizan la unidad de las variables
res_pca <- PCA(scale(datos_acp1), graph = FALSE)

# Autovectores
res_pca$svd$V

# Autovalores y porcentaje de varianza explicada
eig_val <- get_eigenvalue(res_pca)
eig_val

mean(eig_val[, 1])

# Varianza Explicada por dimensión
fviz_eig(res_pca, addlabels = TRUE, ylim = c(0, 50))

# Correlación de cada variable con la dimensión
var <- res_pca$var
corrplot(var$cor, is.corr = FALSE)

# Grabar los datos y los resultados de los scores en un archivo CSV
salida_acp <- res_pca$ind$coord[, 1:4]
salida_acp

# Componente principal por variable en dos dimensiones
fviz_pca_var(
  res_pca,
  axes = c(3, 4),
  repel = TRUE,
  col.var = "blue"
) + theme_gray()

# componente principal por variable en tres dimensiones
plot3d(var$coord[, 1:3])
text3d(var$coord[, 1:3], texts = rownames(var$coord), col = "blue")
coords <- NULL
for (i in 1:nrow(var$coord[, 1:3])) {
  coords <- rbind(coords, rbind(c(0, 0, 0), var$coord[, 1:3][i, 1:3]))
}
lines3d(coords, col = "blue", lwd = 3)

# Componentes principales por grupos y variables
set.seed(123)
res_km <- kmeans(var$coord[, 1:3], centers = 3, nstart = 25)
grp <- as.factor(res_km$cluster)
fviz_pca_var(res_pca,
  col.var = grp,
  palette = c("#0073C2FF", "#EFC000FF", "#868686FF"),
  legend.title = list(fill = "Variedad", color = "Cluster"),
  repel = TRUE
) + theme_gray()
