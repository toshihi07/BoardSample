package boardSample.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sun.xml.bind.v2.runtime.unmarshaller.Intercepter;

import boardSample.AccountUserDetails;
import boardSample.Interceptor;
import boardSample.PageWrapper;
import boardSample.entity.Board;
import boardSample.entity.Comment;
import boardSample.entity.ExcelBuilder;
import boardSample.entity.User;
import boardSample.form.ImageForm;
import boardSample.service.BoardService;
import boardSample.service.CommentService;
import boardSample.service.UserService;

@Controller
@RequestMapping("/boards/{id}/comments")
public class CommentController{
	
	@Autowired
	private CommentService commentService;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Interceptor intercepter;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return "ajax/index";
	}
	
	@GetMapping("new") 
	public String newComment(@PathVariable ("id") int board_id,Model model) {
		Board board = boardService.getBoard(board_id);
		model.addAttribute("board",board);
		return "comment/commentNew";
	}
	
	@PostMapping(value = "create") 
	@ResponseBody
	public Map create(@PathVariable ("id") int board_id,@RequestParam("title") String title,@RequestParam("text") String text,@RequestParam("image") MultipartFile image
) throws Exception {
		Board board = boardService.getBoard(board_id);
		Comment comment = new Comment();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountUserDetails subject = (AccountUserDetails) auth.getPrincipal();
        User user = subject.getUser();
        comment.setBoard(board);
		comment.setText(text);
		comment.setTitle(title);
		comment.setUser(user);
		//formから送られてきた画像を保存サイズの出力。
	    //StringBufferクラスをnewする。StringBufferクラスは、Stringと違って文字列操作が可能。
		String base64 = new String(Base64.encodeBase64(image.getBytes()),"ASCII");
	    comment.setImage(base64);
		commentService.save(comment);
		//コメント件数と最新投稿日時の更新
		int commentsSize = commentService.findByBoard_BoardId(board_id);
		board.setComments_size(commentsSize);
		Date LastCommentDate = new Date();
		board.setLast_comment_date(LastCommentDate);
		boardService.save(board);
		
		Map<String, String> res = new HashMap<>();
		String commentId = String.valueOf(comment.getCommentId());
		String boardId = String.valueOf(comment.getBoard().getBoardId());
		String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getCreated_at());
		String imageText = "";
		if (comment.getImage() == "") {
			imageText = "";
		} 
		String boardName = board.getName();
        int comments_size = commentService.findByBoard_BoardId(board_id);
        String comments_size1 = String.valueOf(comments_size);
		res.put("username", comment.getUser().getUsername());
		res.put("commentId", commentId);
		res.put("boardId", boardId);
		res.put("title",comment.getTitle());
		res.put("text", comment.getText());
		res.put("imageText", imageText);
		res.put("createdAt", createdAt);
		res.put("boardName",boardName);
		res.put("commentsSize",comments_size1);
		return res;
	}
		
	@GetMapping("{comment_id}/edit")
	public String edit(@PathVariable int comment_id, Model model) {
		Comment comment = commentService.getComment(comment_id);
		Board board = comment.getBoard();
		StringBuffer data = new StringBuffer();
		String base64 = comment.getImage();
	    comment.setImage(base64);
	    data.append("data:image/jpeg;base64,");
	    data.append(base64);
	    int board_id = board.getBoardId();
	    model.addAttribute("base64",base64);
	    model.addAttribute("base64image",data.toString());
	    model.addAttribute("comment", comment);
	    model.addAttribute("board",board);
	    model.addAttribute("board_id",board_id);
	    model.addAttribute("comment_id", comment_id);
	    return "comment/commentEdit";
	}
	
	//詳細
	@GetMapping("{comment_id}")
	public String show(@PathVariable int comment_id, Model model) {
		Comment comment = commentService.getComment(comment_id);
		Board board = comment.getBoard();
		int board_id = comment.getBoard().getBoardId();
		//画像データ処理
		StringBuffer data = new StringBuffer();
		String base64 = comment.getImage();
	    comment.setImage(base64);
	    data.append("data:image/jpeg;base64,");
	    data.append(base64);
	    model.addAttribute("boardName",board.getName());
	    model.addAttribute("base64",base64);
	    model.addAttribute("base64image",data.toString());
	    model.addAttribute("comment", comment);
	    model.addAttribute("board_id", board_id);
        //ログイン確認
        Boolean login = intercepter.isUserLogged();
        model.addAttribute("login", login);
	    return "comment/commentShow";
	}
	
	//更新
	@PostMapping("{comment_id}/update")
	public String update(@PathVariable("comment_id") int comment_id, @PathVariable("id") int id,@ModelAttribute Comment comment,BindingResult result1,@ModelAttribute ImageForm imageForm,BindingResult result2) throws UnsupportedEncodingException, IOException {
		//画像の保存処理
	    System.out.println(imageForm.getImage());
		String base64 = new String(Base64.encodeBase64(imageForm.getImage().getBytes()),"ASCII");
		Comment comment1 = commentService.getComment(comment_id);
		comment1.setTitle(comment.getTitle());
		comment1.setText(comment.getText());
		comment1.setImage(base64);
	    commentService.update(comment1);
	    String sId = String.valueOf(id);
	    return "redirect:/boards/" + sId;
	}
	
	//削除
	@PostMapping("{comment_id}")
	public String destroy(@PathVariable("comment_id") int comment_id,@PathVariable("id") int id) {
		Comment comment = commentService.getComment(comment_id);
	    commentService.delete(comment);
	    //文字列に変換する
	    String sId = String.valueOf(id);
	    return "redirect:/boards/" + sId;
	}
	
	//検索
	
	@GetMapping("searchWord")
	public String searchWord(@PathVariable ("id") int board_id,@RequestParam String word,Model model,ModelMap modelMap,Pageable pageable) {
 		Board board = boardService.getBoard(board_id);
	    Page<Comment> commentPages = commentService.findByWordLike(word,board,pageable);
        PageWrapper<Comment> page = new PageWrapper<Comment>(commentPages,"/boards/{id}");
	    model.addAttribute("page", page);
        model.addAttribute("comments", page.getContent());
        
        //検索結果件数
        List<Comment> Comment_list = commentService.findByTitleLike(word, board);
        int comments_size = Comment_list.size();
        if (comments_size == 0) {
        	 comments_size = 0;
        }
        String SearchType = "キーワード検索結果:";
        model.addAttribute("SearchType",SearchType);
        model.addAttribute("comments_size",comments_size);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountUserDetails subject = (AccountUserDetails) auth.getPrincipal();
        User user = subject.getUser();
        int user_id = user.getUserId();
 		model.addAttribute("loginUser_id", user_id);
 		
		model.addAttribute("board", board);
		model.addAttribute("boardName", board.getName());
		model.addAttribute("board_id",board_id);
        //ログイン確認
        Boolean login = intercepter.isUserLogged();
        model.addAttribute("login", login);
		return "board/boardSearch";
	}
	
	@GetMapping("searchTitle")
	public String searchTitle(@PathVariable ("id") int board_id,@RequestParam String title,Model model,ModelMap modelMap,Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountUserDetails subject = (AccountUserDetails) auth.getPrincipal();
        User user = subject.getUser();
        int user_id = user.getUserId();
 		model.addAttribute("loginUser_id", user_id);
 		Board board = boardService.getBoard(board_id);
		Page<Comment> comments = commentService.findByTitleLike(title, board, pageable);
		model.addAttribute("board", board);
		model.addAttribute("boardName", board.getName());
		model.addAttribute("comments",comments);
		model.addAttribute("board_id",board_id);
        //検索結果件数
        List<Comment> Comment_list = commentService.findByTitleLike(title, board);
        int comments_size = Comment_list.size();
        if (comments_size == 0) {
        	 comments_size = 0;
        }
        String SearchType = "タイトル検索結果:";
        model.addAttribute("SearchType",SearchType);
        model.addAttribute("comments_size",comments_size);
        //ログイン確認
        Boolean login = intercepter.isUserLogged();
        model.addAttribute("login", login);
		return "board/boardSearch";
	}
	
	@GetMapping("searchText")
	public String searchText(@PathVariable ("id") int board_id,@RequestParam String text,Model model,ModelMap modelMap,Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountUserDetails subject = (AccountUserDetails) auth.getPrincipal();
        User user = subject.getUser();
        int user_id = user.getUserId();
 		model.addAttribute("loginUser_id", user_id);
 		Board board = boardService.getBoard(board_id);
		Page<Comment> comments = commentService.findByTextLike(text, board, pageable);
		model.addAttribute("board", board);
		model.addAttribute("boardName", board.getName());
		model.addAttribute("comments",comments);
		model.addAttribute("board_id",board_id);
        //検索結果件数
        List<Comment> Comment_list = commentService.findByTextLike(text, board);
        int comments_size = Comment_list.size();
        if (comments_size == 0) {
        	 comments_size = 0;
        }
        String SearchType = "テキスト検索結果:";
        model.addAttribute("SearchType",SearchType);
        model.addAttribute("comments_size",comments_size);
        //ログイン確認
        Boolean login = intercepter.isUserLogged();
        model.addAttribute("login", login);
		return "board/boardSearch";
	}
	//エクセルファイルダウンロード
	@PostMapping("/download")
	public ModelAndView download(@PathVariable ("id") int board_id) throws Exception {
	List<Comment> comments = commentService.findAll(board_id);
	ModelAndView mav = new ModelAndView(new ExcelBuilder());
	Board board = boardService.getBoard(board_id);
	String board_name = board.getName();
	mav.addObject("comments", comments);
	mav.addObject("fileName", "ポケモン育成論" + ".xls");
	mav.addObject("board_name", board_name);
	return mav;
	}	
	//エクセルファイルアップロード
	@PostMapping("/upload")
	public String upload(@RequestParam ("upload_file") MultipartFile uploadFile, Model model,@PathVariable int id) throws Exception {
		//アップロードしたファイルをisで読み込み
	    InputStream in = uploadFile.getInputStream();
		//アップロードしたエクセルファイルにアクセス。
	    Workbook workbook = WorkbookFactory.create(in);
	    // シートのインスタンス取得
	    Sheet sheet = workbook.getSheetAt(0);
	    // 有効行数の確認
	    int rowCount = sheet.getLastRowNum();

	    Comment comments[] = new Comment[rowCount];
	    		
	    for (int i = 0; i < rowCount;i++) {
	    	Row row = sheet.getRow(i + 1);
	    	Cell titleCell = row.getCell(1);
	        String titleValue = titleCell.getStringCellValue();
	    	Cell textCell = row.getCell(2);
	        String textValue = textCell.getStringCellValue();
	    	Cell boardIdCell = row.getCell(3);
	    	double boardIDValue = boardIdCell.getNumericCellValue();
	    	int board_id = (int)boardIDValue;
	    	Cell userIdCell = row.getCell(4);
	    	double userIDValue = userIdCell.getNumericCellValue();
	    	int user_id = (int)userIDValue;
	    	Cell imageCell = row.getCell(5);
	    	String imageValue = imageCell.getStringCellValue();	
	    	comments[i] = new Comment(titleValue,textValue,imageValue);
	    	Board board = boardService.getBoard(board_id);
	    	User user = userService.getUser(user_id);
	    	comments[i].setBoard(board);   
	    	comments[i].setUser(user);
	    	commentService.save(comments[i]);
	    	}
	    
	    model.addAttribute("comments",comments);
	    model.addAttribute("id",id);
		return "comment/commentImpotResult";
	}
}
