package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

    @Mock
    private UserService userService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScoreRepository repository;

    private UserEntity clientOrAdmin;
    private Long existingMovieId, nonExistingMovieId;
    private MovieEntity movie;
    private MovieDTO movieDTO;
    private ScoreDTO scoreDTO;
    private ScoreEntity score;

    @BeforeEach
    void setUp() {
        clientOrAdmin = UserFactory.createUserEntity();
        movie = MovieFactory.createMovieEntity();
        movieDTO = MovieFactory.createMovieDTO();
        score = ScoreFactory.createScoreEntity();
        scoreDTO = ScoreFactory.createScoreDTO();
        movie.getScores().add(score);

        existingMovieId = 1L;
        nonExistingMovieId = 2000L;

        Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));
        Mockito.when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

        Mockito.when(movieRepository.save(ArgumentMatchers.any())).thenReturn(movie);
        Mockito.when(repository.saveAndFlush(ArgumentMatchers.any())).thenReturn(score);
    }

    @Test
	public void saveScoreShouldReturnMovieDTO() {
        Mockito.when(userService.authenticated()).thenReturn(clientOrAdmin);

        MovieDTO result = service.saveScore(scoreDTO);

        Assertions.assertNotNull(result);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
        Mockito.when(userService.authenticated()).thenReturn(clientOrAdmin);
        ScoreDTO scoreDTO1 = new ScoreDTO(nonExistingMovieId, scoreDTO.getScore());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           service.saveScore(scoreDTO1);
        });
	}
}
