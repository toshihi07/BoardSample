package boardSample.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import boardSample.service.CommentService;

@Entity
@Table(name = "boards")
public class Board implements Serializable{
	
//	@Autowired
//	private CommentService commentService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "board_id")
	private int boardId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "created_at")
    private Date created_at;
		
    @PrePersist
    public void onPrePersist() {
        setCreated_at(new Date());
    }
    
	@Column(name = "comments_size")
	private int comments_size;
	
	@Column(name = "last_comment_date")
	private Date last_comment_date;
	
	//commentのプロパティ要素をmappedBy要素に指定。
	//cascade属性を指定すると、自身への操作を関連Entityへも伝搬させることが出来る。
	@OneToMany(mappedBy = "board")
	private List<Comment> comments;


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Date getLast_comment_date() {
		return last_comment_date;
	}
	public void setLast_comment_date(Date last_comment_date) {
		this.last_comment_date = last_comment_date;
	}
	
	public int getComments_size() {
		return comments_size;
	}
	public void setComments_size(int comments_size) {
		this.comments_size = comments_size;
	}
	
}
