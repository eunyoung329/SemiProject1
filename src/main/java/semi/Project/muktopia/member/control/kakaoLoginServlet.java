package semi.Project.muktopia.member.control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import semi.Project.muktopia.member.model.service.MemberService;
import semi.Project.muktopia.member.model.vo.Member;

@WebServlet("/member/kakaoLogin")
public class kakaoLoginServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginID = req.getParameter("kakaoId");
		String kakaoNick = req.getParameter("kakaoNick");
		
		System.out.println("loginID : " + loginID);
		System.out.println("loginPw : " + kakaoNick);
		
		Member mem = new Member();
		
		mem.setMemberEmail(loginID);
		mem.setMemberNick(kakaoNick);
		
		try {
			MemberService service = new MemberService();
			//이메일, 비밀번호가 일치하는 회원을 조회하는 서비스 호출 후 결과 반환 받기
			Member loginMember = service.loginKakao(mem);
			
			//session scope
			//로그인
			//id/pw가 일치하는 회원 정보를 Session scope에 세팅하는 것.
			//session 객체 얻어오기
			HttpSession session = req.getSession();
			if(loginMember!=null) {
				// 회원 정보 Session 세팅
				session.setAttribute("loginMember", loginMember);
				//특정 시간 동안 요청이 없으면 세션 만료
				session.setMaxInactiveInterval(3600);//초단위로 작성하는 것
				
				Cookie c = new Cookie("saveId",loginID);
				if(req.getParameter("saveId")!=null) {
					
					c.setMaxAge(60*60*24*30);//30일
				}else {
					
					c.setMaxAge(0);
				}
				c.setPath(req.getContextPath());
				
				resp.addCookie(c);
				
				
				resp.sendRedirect(req.getContextPath());
				//리다이렉트할 장소를 적어
//				req.getRequestDispatcher("../index.jsp").forward(req, resp);
			}else {
				
				//session.setAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
			}
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
