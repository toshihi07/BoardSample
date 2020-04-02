package boardSample.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import boardSample.entity.Board;
import boardSample.entity.Comment;
import boardSample.repository.CommentRepository;

@Service
public class CommentService {
	@Autowired
	CommentRepository commentRepository;

	public List<Comment> findAll(int board_id) {
		List<Comment> comments = commentRepository.findByBoard_BoardId(board_id);
		return comments;
	}
	
	public Page<Comment> findAll(int board_id,Pageable pageable) {
		Page<Comment> comments = commentRepository.findByBoard_BoardId(board_id,pageable);
		return comments;
	}

	public void save(Comment comment) {
		commentRepository.saveAndFlush(comment);
	}
	
	public void update(Comment comment) {
		commentRepository.saveAndFlush(comment);
	}
	
	public void delete(Comment comment) {
		commentRepository.delete(comment);
	}

	public Comment getComment(Integer id) {
		return commentRepository.getOne(id);
	}
	
	@Transactional
    public Page<Comment> findByWordLike(String word,Board board,Pageable pageable) {
        return commentRepository.findByWordLike(word,board,pageable);
    }
	
	@Transactional
    public Page<Comment> findByTitleLike(String title,Board board,Pageable pageable) {
        return commentRepository.findByTitleLike(title,board,pageable);
    }
	
	@Transactional
    public Page<Comment> findByTextLike(String text,Board board,Pageable pageable) {
        return commentRepository.findByTextLike(text,board,pageable);
    }
	
	
}
