package com.example.dynamodb;

import com.example.dynamodb.dynamodbtestextension.DynamoDbTestTables;
import com.example.dynamodb.dynamodbtestextension.DynamoDbTestExtension;
import com.example.dynamodb.dynamodbtestextension.DynamoDbTestLoadData;
import com.example.dynamodb.infraestructure.database.FilmRepositoryImpl;
import com.example.dynamodb.infraestructure.database.model.FilmDocument;
import com.example.dynamodb.scenes.films.FilmsCreateTables;
import com.example.dynamodb.scenes.films.FilmsLoadData;
import com.example.dynamodb.scenes.films.SearchFilmsLoadData;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;


@Slf4j
@SpringBootTest
@ExtendWith(DynamoDbTestExtension.class)
@DynamoDbTestTables(FilmsCreateTables.class)
class FilmRepositoryImplTest {

  @Autowired
  FilmRepositoryImpl filmRepository;

  @Test
  @DisplayName("given valid film when create a film then success created")
  void create_film_success() {

    var createFilm = FilmDocument.builder()
        .id(UUID.randomUUID())
        .originalTitle("Test film original title")
        .build();

    var result = filmRepository.create(createFilm)
        .onSuccess(success -> log.info("Correct persist film"))
        .onFailure(failure -> log.error("Error persist film", failure));

    BDDAssertions.then(result.isSuccess()).isTrue();

  }

  @Test
  @DisplayName("given exist id film when delete then return success")
  @DynamoDbTestLoadData(FilmsLoadData.class)
  void delete_film_success(){

    var existId = UUID.fromString("e536ba01-55a3-4a04-95e8-1ecec1c378a5");

    var result = filmRepository.deleteById(existId);

    BDDAssertions.then(result.isSuccess()).isTrue();

  }

  @Test
  @DisplayName("given exist film whe update then return success")
  @DynamoDbTestLoadData(FilmsLoadData.class)
  void update_film_success(){

    var updateFilm = FilmDocument.builder()
        .id(UUID.fromString("e536ba01-55a3-4a04-95e8-1ecec1c378a5"))
        .originalTitle("Test updated film original title")
        .build();


    var result = filmRepository.update(updateFilm);

    BDDAssertions.then(result.isSuccess()).isTrue();
    BDDAssertions.then(result.get().getOriginalTitle()).isEqualTo(updateFilm.getOriginalTitle());

  }

  @Test
  @DisplayName("given exist id film when find by id then return success")
  @DynamoDbTestLoadData(FilmsLoadData.class)
  void find_by_id_film_success() {

    var existId = UUID.fromString("e536ba01-55a3-4a04-95e8-1ecec1c378a5");

    var result = filmRepository.findById(existId);

    BDDAssertions.then(result.isSuccess()).isTrue();
    BDDAssertions.then(result.get().getId()).isEqualTo(existId);

  }

  @Test
  @DisplayName("given title of film exist when search then return success")
  @DynamoDbTestLoadData(SearchFilmsLoadData.class)
  void search_film_by_title_success(){

    var title = "Test original title";

    var result = filmRepository.findByTitle(title);

    BDDAssertions.then(result.isFailure()).isFalse();
    BDDAssertions.then(result.get()).isNotEmpty();

  }

  @Test
  @DisplayName("given not exist film whe update then return failure")
  @DynamoDbTestLoadData(FilmsLoadData.class)
  void update_film_failure(){

    var updateFilm = FilmDocument.builder()
        .id(UUID.fromString("e536ba01-55a3-4a04-95e8-1ecec1c37866"))
        .originalTitle("Test updated film original title")
        .build();

    var result = filmRepository.update(updateFilm);

    BDDAssertions.then(result.isFailure()).isTrue();

  }

  @Test
  @DisplayName("given not exist id film when delete then return failure")
  @DynamoDbTestLoadData(FilmsLoadData.class)
  void delete_film_failure(){

    var existId = UUID.fromString("e536ba01-55a3-4a04-95e8-1ecec1c37866");

    var result = filmRepository.deleteById(existId);

    BDDAssertions.then(result.isFailure()).isTrue();

  }

  @Test
  @DisplayName("given not valid film when create a film then failure")
  void create_film_failure() {

    var createFilm = FilmDocument.builder().build();

    var result = filmRepository.create(createFilm)
        .onSuccess(success -> log.info("Correct persist film"))
        .onFailure(failure -> log.error("Error persist film", failure));

    BDDAssertions.then(result.isFailure()).isTrue();

  }

  @Test
  @DisplayName("given note exist id film when find by id then return failure")
  @DynamoDbTestLoadData(FilmsLoadData.class)
  void find_by_id_film_failure() {

    var existId = UUID.fromString("e536ba01-55a3-4a04-95e8-1ecec1c37866");

    var result = filmRepository.findById(existId);

    BDDAssertions.then(result.isFailure()).isTrue();

  }

}
