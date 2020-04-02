package boardSample.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import boardSample.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board,Integer>{
	@Query("SELECT b FROM Board b WHERE b.name LIKE %:name%")
	Page<Board> findByBoardNameLike(@Param("name") String name,Pageable pageble);
		}

 
