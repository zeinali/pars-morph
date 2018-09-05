# Pars Morph: A Persian Morphological Analyzer

Pars Morph is a rule-based Persian morphological analysis system, which analyzes the internal structure of word in Persian and determines the grammatical category and function of the word parts.
Being in link with a lexicon covering about 45000 lexemes and based on morpho-logical rules in Persian, it can analyze the structure of complex lexemes and their possible inflected forms and even out-of-lexicon words.

For the original paper (written in Persian), see: [Pars Morph: A Persian Morphological Analyzer](https://mavaji.github.io/2012/05/16/parsmorph.html)

## How to run:
### Create MySQL database:
```sql
CREATE DATABASE parsmorph CHARACTER SET utf8 COLLATE utf8_general_ci;
```

### Import database:
Unzip ```db/parsmorph.zip```, then: 
```shell
mysql -uroot -p parsmorph < parsmorph.sql
```

### Build and run: 
```shell
mvn spring-boot:run
```

### API Documentation
```
http://localhost:8080/swagger-ui.html
```

### Morphological Analysis
With [cURL](https://curl.haxx.se/):
```shell
curl -G -v "http://localhost:8080/possibilities/" --data-urlencode "q=دانشجویانشانند"
```
Via browser or [Postman](https://www.getpostman.com/):
```
http://localhost:8080/possibilities/?q=دانشجویانشانند
```

### Text to Phoneme
With [cURL](https://curl.haxx.se/):
```shell
curl -G -v "http://localhost:8080/phonemes/" --data-urlencode "q=مردم حضور دارند"
```
Via browser or [Postman](https://www.getpostman.com/):
```
http://localhost:8080/possibilities/?q=مردم حضور دارند
```
