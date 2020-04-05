package boardSample.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import boardSample.entity.Board;
import boardSample.repository.BoardRepository;

@Service
@Transactional
public class BoardService {
	
	@Autowired
	BoardRepository boardRepository;
	
	
	
	public Page<Board> findAll(Pageable pageable) {
	    Page<Board> page = boardRepository.findAllOrderByBoardIdDesc(pageable);
		return page;
	}
	
	@Transactional
	public Board getBoard(Integer id) {
		return boardRepository.getOne(id);
	}
	
	@Transactional
	public void save(Board board) {
		boardRepository.saveAndFlush(board);
	}
	
	@Transactional
    public Page<Board> findByBoardNameLike(String name,Pageable pageable) {
		return boardRepository.findByBoardNameLike(name,pageable);
    }
}
