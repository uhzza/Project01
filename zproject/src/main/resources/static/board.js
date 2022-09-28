/*
	MVC 방식(실제 화면은 백에서 처리)
		- 서버의 응답이 ModelAndView(데이터와 타임리프/jsp)
		- 타임리프나 jsp에서 자바 코드를 이용해 데이터를 직접 출력할 수 있다
		<c:forEach items="${boardList} var="board">
			<tr>
				<td>${board.bno}</td>
				<td>${board.title}</td>
			</tr>
		</c:forEach>
		
		- jQuery하고 무슨 차이?
			타임리프/jsp는 서버에서 처리한 다음 사용자에게는 결과 html를 보내준다
			즉 사용자는 5~10라인의 타임리프/jsp 소스를 절대로 볼 수가 없다
	REST 방식
		- 서버는 데이터를 사용자에게 보낸다. 화면은 사용자가 알아서 만든다
		- CSR(Client Side Rendering)
	요즘 들어서 Vue.js, React.js등 자바스크립트쪽에서 다시금 서버에서 화면을 만드는 방식이 등장
	SSR(Server Side Rendering)
*/

// 글번호 :110, 제목:110번글입니다, 내용: 없어요, 글쓴이: spring
// 댓글 3개 - {댓글번호, 내용, 글쓴이}
export const board = {
	bno: 110,
	title: '110번글입니다',
	content:'없어요',
	writer:'spring',
	comments: [
		{cno: 3, content: '3번 댓글', writer: 'spring' },
		{cno: 2, content: '2번 댓글', writer: 'summer' },
		{cno: 1, content: '1번 댓글', writer: 'spring' }
	]
}
 






