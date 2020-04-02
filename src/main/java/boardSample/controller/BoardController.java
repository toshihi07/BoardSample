package boardSample.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import boardSample.AccountUserDetails;
import boardSample.PageWrapper;
import boardSample.entity.Board;
import boardSample.entity.Comment;
import boardSample.entity.User;
import boardSample.service.BoardService;
import boardSample.service.CommentService;

@Controller
@RequestMapping("/")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping
	public String index(Pageable pageable,Model model,ModelMap modelMap) {
	    Page<Board> boardPages = boardService.findAll(pageable);
	    pageable = PageRequest.of(0, 10);
	    model.addAttribute("page", boardPages);
        model.addAttribute("boards", boardPages.getContent());
        model.addAttribute("url", "/board/boardIndex");
		return "/board/boardIndex";
	    }
	
	@GetMapping("/boards/new")
	public String newBoard(Model model) {
		return "board/boardNew";
	}
	
	@PostMapping()
	public String create(@ModelAttribute Board board,Model model,ModelMap modelMap, Principal principal){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountUserDetails subject = (AccountUserDetails) auth.getPrincipal();
        User user = subject.getUser();
	    board.setUser(user);
	    boardService.save(board);
		return "redirect:/";
	}
	
	@GetMapping("/boards/{id}")
	public String show(@PathVariable int id, Model model,Pageable pageable) {
		//page処理
		pageable = PageRequest.of(0, 10);
	    Page<Comment> commentPages = commentService.findAll(id,pageable);
        PageWrapper<Comment> page = new PageWrapper<Comment>(commentPages,"/board/boardShow");
	    model.addAttribute("page", page);
        model.addAttribute("comments", page.getContent());
        model.addAttribute("url", "/board/boardShow");
      
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountUserDetails subject = (AccountUserDetails) auth.getPrincipal();
        User user = subject.getUser();
        int user_id = user.getUserId();
 		model.addAttribute("loginUser_id", user_id);
		Board board = boardService.getBoard(id);
 		model.addAttribute("board", board);
		model.addAttribute("boardName", board.getName());		
		model.addAttribute("board_id",id);
		return "board/boardShow";
	}
	
	@GetMapping("/boards/search")
	public String boardSearch(@RequestParam String name,Model model,Pageable pageable) {
	    pageable = PageRequest.of(0, 10);
		Page<Board> boardPages = boardService.findByBoardNameLike(name,pageable);
	    model.addAttribute("page", boardPages);
        model.addAttribute("boards", boardPages.getContent());
        model.addAttribute("url", "/board/boardIndex");
		return "/board/boardIndex";
	}

}


