TuentiContest-2012
==================

Repo for sharing my solutions to Tuenti Contest 2012

No estoy demasiado contento con las soluciones de este año, al menos no he tenido que hacer skip pero me he quedado atascadisimo en el 19.

Al menos soy consciente de haber hecho submits con errores en:
* 17 - Las pruebas con los tethas son arbitrarias y pueden dar lugar a falsos negativos.
* 12 - No llegue a sacar la info de la imagen, consegui romper las dos primeras keys y googleando probé varias posibilidades hasta llegar a courage.
* 11 - Hice dos algoritmos completamente distintos, el primero era genial para situaciones normales, pero para bolsas de 25 letras era infumable, todas estas pruebas me llevaron demasiado tiempo y tampoco quedé muy contento con el resultado, el preproceso es demasiado lento.
* 9 - Fallo en el submit, en la versión que envié probe a cachear los ficheros en memoria, para 10 o 20 era una maravilla pero obviamente peto en el submit, notese el try catch con sleep a partir de entonces.
* 8 - Mi mayor fallo hasta un día despues no cai en que podía hacer la expansion por bloques en memoria e ir haciendo updates del MD5, soy así de torpe que le vamos a hacer, eso sí consegui que la lectura/escritura fuera como 20 veces más rapido que con las primeras pruebas.
* 7 - Algoritmo precioso para mapear todas las soluciones, pero inviable el imprimirlas desde esa representación, creo que tarda mas en generar las soluciones que en crackear la clave :(
* 4 - En uno de los dos me dió overflow en el submit generando soluciones negativas :(