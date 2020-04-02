package boardSample.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import boardSample.entity.Board;
import boardSample.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
	Page<Comment> findByBoard_BoardId(int board_id,Pageable pageble);
	List<Comment> findByBoard_BoardId(int board_id);

	
	@Query("SELECT c FROM Comment c WHERE c.board LIKE :board AND c.title LIKE %:word% OR c.text LIKE %:word%")
	Page<Comment> findByWordLike(@Param("word") String word,Board board,Pageable pageble);
	
	@Query("SELECT c FROM Comment c WHERE c.board LIKE :board AND c.title LIKE %:title%")
	Page<Comment> findByTitleLike(@Param("title") String title,Board board,Pageable pageble);
	
	@Query("SELECT c FROM Comment c WHERE c.board LIKE :board AND c.text LIKE %:text%")
	Page<Comment> findByTextLike(@Param("text") String text,Board board,Pageable pageble);
		}
