#!/bin/sh

# Primeiro argumento = tamanho da tabela
# Segundo argumento  = quantidade de numeros gerados
# Terceiro argumento = metodo de hashing
#                        0 - Modulo
#                        1 - XOR
#                        2 - Shift de bits

IFS=" "
for t in 10 100 1000; do 
    for n in 100 1000 10000 20000 100000; do
        for m in 0 1 2; do
            java Hash $t $n $m
        done
    done
done
for t in 10000 20000; do
    for n in 20000 100000 200000 400000 500000; do
        for m in 0 1 2; do
            java Hash $t $n $m
        done
    done
done
