## Requerimientos funcionales ##

Queremos construir una herramienta de línea de comando para indexar y buscar documentos almacenados en el sistema de 
archivos de la computadora.

La herramienta permitirá al usuario realizar los siguientes casos de uso.

![Casos de uso](./design/UseCases.svg)

El caso de uso principal que queremos resolver es el de **Buscar archivos por términos**. De manera tal que el usuario pueda 
ejecutar un comando en la terminal que reciba una secuencia de términos y que retorne todos los documentos indexados del 
sistema de archivos que contengan todos los términos especificados.

```
> buscar cucharada vainilla azúcar
/home/juanperez/Documentos/recetas/queque_seco.txt
/home/juanperez/Descargas/flan.txt
/home/juanperez/Documentos/revisar/remedios.txt
```

En este caso el usuario `juanperez` indexó su carpeta `home` en un momento anterior a la ejecución del buscador.

Los archivos `queque_seco.txt`, `flan.txt`, y `remedios.txt` todos contienen los términos `cucharada`, `vainilla` y 
`azúcar`.

Para **Indexar un directorio** el usuario ejecutará un comando especificando la ruta del directorio a indexar. El 
proceso recorrerá el directorio recursivamente buscando todos los archivos de texto que contenga; para cada uno de estos 
archivos leerá su contenido palabra por palabra asociando en un índice cada palabra con la ruta al archivo en cuestión.

```
> indexar /home/juanperez
Indexando /home/juanperez
Indexando /home/juanperez/Documentos
Indexando /home/juanperez/Documentos/recetas
Indexando /home/juanperez/Documentos/revisar
Indexando /home/juanperez/Documentos/nueva_carpeta
Indexando /home/juanperez/Descargas
```

El arquitecto del proyecto ha sugerido que utilicemos un `Trie` como estructura de datos para el índice. Esta estructura 
cumple con el siguiente contrato:

```java
interface Trie<V> {
    void insert(String key, V value);
    List<V> find(String key);
}
```

El índice se almacenará en un archivo oculto que llamaremos por defecto `.index` y que estará localizado en la carpeta 
`home` del usuario.

Aunque para la primera versión sólo soportaremos archivos de texto plano con extensión `.txt`, sabemos que para una 
posterior versión nuestro buscador también tendrá que soportar otro tipo de documentos de texto como por ejemplo 
`.odt` o `.pdf`.

Nuestra tarea es diseñar esta herramienta de software, tomando en cuenta los requerimientos anteriormente especificados,
a la vez que incorporamos previsiones para acomodar los requerimientos futuros sobre los que nos han avisado.
