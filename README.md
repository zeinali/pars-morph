# [Pars Morph: A Persian Morphological Analyzer](http://jsdp.rcisp.ac.ir/article-1-714-en.html)

```sql
CREATE DATABASE parsmorph CHARACTER SET utf8 COLLATE utf8_general_ci;
```

```shell
mysql -uroot -p parsmorph < parsmorph.sql
```

```bash
mvn clean package
```

```bash
mvn spring-boot:run
```

```shell
curl -G -v "http://localhost:6666/possibilities/" --data-urlencode "q=دانشجویانشانند"
```