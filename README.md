# [Pars Morph: A Persian Morphological Analyzer](http://jsdp.rcisp.ac.ir/search.php?sid=1&slc_lang=en&auth=Mavaji)

```sql
CREATE DATABASE parsmorph CHARACTER SET utf8 COLLATE utf8_general_ci;
```

```shell
mysql -uroot -p parsmorph < parsmorph.sql
```

```shell
curl -G -v "http://localhost:6666/possibilities/" --data-urlencode "q=دانشجویانشانند"
```