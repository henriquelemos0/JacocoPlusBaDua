JacocoPlusBaDua
===============

Build no eclipse com os parametros -> package -Djdk.version=1.7 -Dbytecode.version=1.7

extrair para o jacoco/target o jacocoant.jar que esta dentro do jacoco/target/jacoco-0.7.1-data.zip/lib/

No Resources/example/build.xml
alterar a linha 4 para o path do .../jacoco/target/jacocoant.jar
alterar a linha 68 para o path do ../Resources/org.jacoco.dataflow-0.0.3-all.jar

dentro de Resources/example/ executar -> ant test


A cada nova alteração é necessario dar um rebuild no projeto, excluir os jar do jacoco/target/ e extrair o jacocoant.jar do novo zip, e executar o ant test novamente.

