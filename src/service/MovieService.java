// MovieService.java
package service;

import model.Movie;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieService {
    
    public boolean addMovie(Movie movie) {
        String query = "INSERT INTO movies (title, description, genre, duration_minutes, " +
                      "release_date, director, cast, category, price, rating, " +
                      "trailer_url, poster_image, banner_image, is_active) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TRUE)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getDescription());
            pstmt.setString(3, movie.getGenre());
            pstmt.setInt(4, movie.getDurationMinutes());
            
            // Handle release date
            if (movie.getReleaseDate() != null) {
                pstmt.setDate(5, Date.valueOf(movie.getReleaseDate()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }
            
            pstmt.setString(6, movie.getDirector());
            pstmt.setString(7, movie.getCast());
            pstmt.setString(8, movie.getCategory());
            pstmt.setDouble(9, movie.getPrice());
            pstmt.setString(10, movie.getRating());
            pstmt.setString(11, movie.getTrailerUrl());
            pstmt.setString(12, movie.getPosterImage());
            pstmt.setString(13, movie.getBannerImage());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM movies WHERE is_active = TRUE ORDER BY release_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Movie movie = extractMovieFromResultSet(rs);
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
    
    public List<Movie> getMoviesByCategory(String category) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM movies WHERE category = ? AND is_active = TRUE ORDER BY release_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Movie movie = extractMovieFromResultSet(rs);
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
    
    public Movie getMovieById(int movieId) {
        String query = "SELECT * FROM movies WHERE movie_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractMovieFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateMovie(Movie movie) {
        String query = "UPDATE movies SET title = ?, description = ?, genre = ?, " +
                      "duration_minutes = ?, release_date = ?, director = ?, " +
                      "cast = ?, category = ?, price = ?, rating = ?, trailer_url = ?, " +
                      "poster_image = ?, banner_image = ?, updated_at = CURRENT_TIMESTAMP " +
                      "WHERE movie_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getDescription());
            pstmt.setString(3, movie.getGenre());
            pstmt.setInt(4, movie.getDurationMinutes());
            
            if (movie.getReleaseDate() != null) {
                pstmt.setDate(5, Date.valueOf(movie.getReleaseDate()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }
            
            pstmt.setString(6, movie.getDirector());
            pstmt.setString(7, movie.getCast());
            pstmt.setString(8, movie.getCategory());
            pstmt.setDouble(9, movie.getPrice());
            pstmt.setString(10, movie.getRating());
            pstmt.setString(11, movie.getTrailerUrl());
            pstmt.setString(12, movie.getPosterImage());
            pstmt.setString(13, movie.getBannerImage());
            pstmt.setInt(14, movie.getMovieId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteMovie(int movieId) {
        String query = "UPDATE movies SET is_active = FALSE WHERE movie_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, movieId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Movie extractMovieFromResultSet(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setMovieId(rs.getInt("movie_id"));
        movie.setTitle(rs.getString("title"));
        movie.setDescription(rs.getString("description"));
        movie.setGenre(rs.getString("genre"));
        movie.setDurationMinutes(rs.getInt("duration_minutes"));
        
        Date releaseDate = rs.getDate("release_date");
        if (releaseDate != null) {
            movie.setReleaseDate(releaseDate.toLocalDate());
        }
        
        movie.setDirector(rs.getString("director"));
        movie.setCast(rs.getString("cast"));
        movie.setCategory(rs.getString("category"));
        movie.setPrice(rs.getDouble("price"));
        movie.setRating(rs.getString("rating"));
        movie.setTrailerUrl(rs.getString("trailer_url"));
        movie.setPosterImage(rs.getString("poster_image"));
        movie.setBannerImage(rs.getString("banner_image"));
        movie.setActive(rs.getBoolean("is_active"));
        
        return movie;
    }
}