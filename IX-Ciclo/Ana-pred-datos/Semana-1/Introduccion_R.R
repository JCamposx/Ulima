#########################################################
###################### CURSO DE R #######################
#########################################################

# =======================================================#
#             PARTE 1: CÁLCULOS MATEMÁTICOS              #
# =======================================================#
# Calculos matemáticos
# --------------------
4 + 4
4 - 5
8 * 9
8 / 9
8^2
8**2
8 + 8 / 9 * (4 + 5)^2 - 10

sqrt(50) # Raiz cuadrada
exp(4) # Exponencial
log(30) # Logaritmo natural o neperiano
log10(30) # Logaritmo en base 10
log(30, base = 4) # Logaritmo en cualquier base
sin(30)
cos(30)
tan(30)
factorial(10) # Factorial de un nÃºmero
choose(5, 2) # Combinatoria
round(4.228, digits = 2)


# =======================================================#
#                  PARTE 2: VECTORES                     #
# =======================================================#
# a) Generación de secuencias
# ---------------------------
# Arreglo
x <- c(1, 5, 3, 4, 8)
x
# Sumamos 2 a todos los elementos del arreglo x
x + 2

# Elevamos al cuadarado a todos los elementos del arreglo x
x^2

# Secuencia de 1 a 10
x <- 1:10
x

# Secuencia de -5 hasta 3
y <- -5:3
y

# Secuencia de 1 a 4, y se le suma 1 a cada valor
1:4 + 1

# Secuencia de 2 a 18, de 2 en 2
x <- seq(from = 2, to = 18, by = 2)
x

# Secuencia de 2 a 18, que tenga una longitud total de 30
x <- seq(from = 2, to = 18, length = 30)
x

# Repetir 1 un total de 5 veces
rep(1, 5)

x <- 1:3
x
rep(x, 2) # Repetir x un total de 2 veces


# b) Generación de secuencias aleatorias
# --------------------------------------
# Seleccionar 3 valores aleatorios entre 1 y 10
sample(10, 3)

# Seleccionar 3 valores aleatorios entre 1 y 10 con una semilla
# Con una semilla, siempre serán los mismos valores
# (Hay que correr las 2 líneas junas)
set.seed(100)
sample(10, 3)

# Seleccionar 10 valores random de 1 al 10 con valores repetidos
sample(10, 10, replace = TRUE)

# Seleccionar 10 valores random de 1 al 10 sin que se repitan los valores
sample(10, 10, replace = FALSE)

# c) Selección de elementos de un vector
# --------------------------------------
x <- 12:20
x

x[1] # Seleccionar el primer valor del arreglo
x[3] # Seleccionar el tercer valor del arreglo

x[-2] # Seleccionar todos los valores del arreglo menos el de la posición 2

x[c(1, 3)] # Seleccionar los valores 1 y 3 del arreglo
x[-c(1, 3)] # Seleccionar todos los valores del arreglo menos 1 y 3

# d) Ordenación de vectores
# --------------------------
x1 <- c(5, 1, 8, 3)
x1

# Ordenar el arreglo x1 (por defecto es de menor a mayor)
sort(x1)

# Ordenar el arreglo x1 de mayor a menor
sort(x1, decreasing = TRUE)

# Ordenar el arreglo x1 de menor a mayor
sort(x1, decreasing = FALSE)

# e) Operaciones en vectores
# --------------------------
x <- c(10, 15, 35, 14, 18, 55)

length(x) # Longitud
sum(x) # Sumar los elementos del arreglo
max(x) # Valor máximo del arreglo
min(x) # Valor mínimo del arreglo
mean(x) # Media del arreglo
median(x) # Mediana del arreglo
quantile(x, 0.75) # Quartil 3 del arreglo
quantile(x, 0.5) # Quartil 2 del arreglo

# Coeficiente de asimetría
as <- function(v) {
        3 * (mean(v) - median(v)) / sd(v)
}

as(x)


# =======================================================#
#                  PARTE 3: MATRICES                     #
# =======================================================#

# a) Creación de matrices
# ----------------------------
# Matriz con valores del 1 al 20
# Con 5 filas
# Con 4 columnas
a0 <- matrix(1:20, nrow = 5, ncol = 4)
a0

# No es necesario definir el número de columnas si ya se definió el número de
# filas
a0 <- matrix(1:20, nrow = 5)
a0

# Matriz con valores del 1 al 20
# Con 5 filas
# Que esté ordenada por columnas
a1 <- matrix(1:20, nrow = 5, byrow = FALSE)
a1

# Matriz con valores del 1 al 20
# Con 5 filas
# Que esté ordenada por filas
a2 <- matrix(1:20, nrow = 5, byrow = TRUE)
a2

#### [fila, columna] ####

a2[3, ] # Seleccionar toda la fila 3
a2[1, ] # Seleccionar toda la fila 1

a2[c(2, 3), ] # Seleccionar las filas 2 y 3

a2[, 2] # Seleccionar toda la columna 2

a2[1, 3] # Seleccionar el valor de fila 1 y columna 3
a2[2, 4] # Seleccionar el valor de fila 2 y columna 4

x <- c(190, 8, 22, 191, 4, 1.7, 223, 80, 2, 210, 50, 3)

# Dado el arreglo x, crear una matriz con 4 filas y ordenado por filas
datos <- matrix(x, nrow = 4, byrow = TRUE)
datos

# Ver las dimensiones
dim(datos) # El resultado es <# filas, # columnas>

# b) Operaciones con matrices
# ----------------------------
A <- matrix(c(12, 11, 3, 5, 45, 34, 34, 23, 34), nrow = 3, ncol = 3, byrow = TRUE)
A

B <- matrix(c(8, 9, 4, 5, 25, 34, 33, 23, 14), nrow = 3, ncol = 3, byrow = FALSE)
B

t(A) # Traspuesta de la matriz
solve(A) # Inversa de la matriz
diag(A) # Matriz diagonal
det(A) # Determinante de una matriz
eigen(A) # Valores y vectores propios

# c) solución de ecuaciones
# -------------------------

# 12X + 11Y +  3Z  = 10
#  5X + 45Y + 34Z  = 20
# 34X + 23Y + 34Z  = 30

b <- c(10, 20, 30) # Solución del sistema de ecuaciones (Ax = b)

solve(A, b) # La matriz A contiene los elementos de la ecuación de arriba

# Lo mismo de arriba, pero dando los resulados en fracciones
library(MASS)
fractions(solve(A, b))


# =======================================================#
#                  PARTE 4: DATA FRAME                   #
# =======================================================#

# a) Crear una base de datos
# --------------------------
altura <- c(1.7, 1.68, 1.80, 1.75, 1.82, 1.58)
peso <- c(80.4, 71.12, 85.7, 75.91, 120.5, 42.0)
hermanos <- c(1, 0, 5, 4, 2, 3)
genero <- c("M", "F", "M", "F", "M", "F")
coef_inte <- c("Alto", "Alto", "Medio", "Bajo", "Alto", "Medio")

# Creamos el data frame,  la base de datos
df <- data.frame(altura, peso, hermanos, genero, coef_inte)
df

# b) Seleccionar variables y observaciones
# ----------------------------------------
# Obtener las columnas por su nombre
df$altura
df$peso

# Igual que arriba, seleccionar la columna altura
df[, "altura"]

# Seleccionar la columna 1
df[, 1]

# Seleccionar las columnas 1 y 4
df[, c(1, 4)]

# Seleccionar las columnas con nombre altura y coef_inte
df[, c("altura", "coef_inte")]

# Seleccionar todo el data frame, menos la columna 2
df[, -2]

# Seleccionar todo el data frame, menos las columnas 2 y 3
df[, -c(2, 3)]

# Seleccionar la fila 2
df[2, ]

# Seleccionar las filas 1, 3 y 5
df[c(1, 3, 5), ]

# Seleccionar las filas 1 y 4, pero solo las columnas 3 y 5
df[c(1, 4), c(3, 5)]

# c) Filtrado
# ------------
df

# Filtrar el data frame que cumplan: coef_inte == "Alto"
df2 <- subset(df, coef_inte == "Alto")
df2

# Filtrar el data frame que cumplan: coef_inte == "Alto" && coef_inte == "Medio"
df3 <- subset(df, coef_inte == c("Alto", "Medio"))
df3

# Filtrar el data frame que cumplan: coef_inte != "Alto"
df4 <- subset(df, coef_inte != "Alto")
df4

# Filtrar el data frame que cumplan: hermanos == 4
df5 <- subset(df, hermanos == 4)
df5

# Filtrar el data frame que cumplan: hermanos > 1
df6 <- subset(df, hermanos > 1)
df6

# Filtrar el data frame que cumplan: hermanos > 1 && genero == "F"
df6 <- subset(df, hermanos > 1 & genero == "F")
df6


# =======================================================#
#            PARTE 5: ESTADÍSTICA DESCRIPTIVA            #
# =======================================================#
# Lectura de datos
library(readxl)

# Leer archivo excel en la ubicación ./MarketingDirecto.xlsx
# En el archivo excel seleccionar la hoja Datos
d_mark <- data.frame(read_excel(
                "./MarketingDirecto.xlsx",
                sheet = "Datos"
        ))
d_mark

# Leer archivo csv en la ubicación ./MarketingDirecto.csv
# El archivo csv está separado por ;
d_mark_csv <- data.frame(read.csv(
        "./MarketingDirecto.csv",
        sep = ";"
))
d_mark_csv

# Entramos al archivo excel, seleccionamos una parte de la tabla y copiamos
# con control+c
d_mark <- read.delim("clipboard") # Selecciona solo lo que hayas copiado
d_mark
# Esto sirve también con otros tipos de archivos como word

head(d_mark)

str(d_mark)

# a) Representación de Datos Cualitativos
# ---------------------------------------

# Tabla de Frecuencia
# -------------------
fi <- table(DMark$Edad)
fri <- prop.table(table(DMark$Edad))
pi <- prop.table(table(DMark$Edad)) * 100
edad_tabla <- t(rbind(fi, fri, pi))
edad_tabla

# Gráfico de Barras
# -----------------
barplot(pi,
        main = "Distribución de las edades de los clientes",
        xlab = "Grupo Etario",
        ylab = "Porcentaje de Clientes"
)

# b) Representación de Datos Cuantitativos Continuos
# --------------------------------------------------

hist(d_mark$Monto)

# Boxplots
boxplot(d_mark$Monto)
boxplot(Monto ~ Edad, d_mark)
boxplot(d_mark[, c("Salario", "Monto")])

# c) Análisis descriptivo
# -----------------------

# Resumen básico
summary(d_mark$Monto)

library(PASWR2)
eda(d_mark$Salario)
