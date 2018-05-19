#Pars Morph: A Persian Morphological Analyzer

```sql
CREATE DATABASE parsmorph CHARACTER SET utf8 COLLATE utf8_general_ci;
```

```jshelllanguage
mysql -uroot -p parsmorph < parsmorph.sql
```

```jshelllanguage
curl -G -v "http://localhost:6666/possibilities/" --data-urlencode "q=دانشجویانشانند"
```