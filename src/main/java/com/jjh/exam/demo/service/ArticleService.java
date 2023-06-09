package com.jjh.exam.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jjh.exam.demo.repository.ArticleRepository;
import com.jjh.exam.demo.utill.Ut;
import com.jjh.exam.demo.vo.Article;
import com.jjh.exam.demo.vo.ResultData;

@Service
public class ArticleService {
	private ArticleRepository articleRepository;

	public ArticleService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	// 왜 해줘야함? 갱신개념??
	// detail
	public Article getForPrintArticle(int actorId, int id) {

		Article article = articleRepository.getForPrintArticle(id);

		updateForPrintData(actorId, article);

		return article;
	}

	// actorId 로그인한사람
	public List<Article> getForPrintArticles(int actorId, int boardId, String searchKeywordTypeCode,
			String searchKeyword, int itemsCountInAPage, int page) {

		int limitStart = (page - 1) * itemsCountInAPage; //
		int limitTake = itemsCountInAPage;

		List<Article> articles = articleRepository.getForPrintArticles(boardId, limitStart, limitTake,
				searchKeywordTypeCode, searchKeyword);

		for (Article article : articles) {
			updateForPrintData(actorId, article);
		}
		return articles;
	}

	// 로그인유저 갱신해줘야함
	private void updateForPrintData(int actorId, Article article) {
		if (article == null) {
			return;
		}
		// 현재 로그인유저가 삭제할수 있냐
		ResultData actorCanDeleeRd = actorCanDelete(actorId, article);
		article.setExtra_actorCanDelete(actorCanDeleeRd.isSuccess());

		ResultData actorCanModifyRd = actorCanModify(actorId, article);
		article.setExtra_actorCanModify(actorCanModifyRd.isSuccess());
	}

	public ResultData actorCanDelete(int actorId, Article article) {
		if (article == null) {
			return ResultData.from("F-1", "게시물이 존재하지않습니다.");
		}
		if (article.getMemberId() != actorId) {
			return ResultData.from("F-2", "권한이 없습니다.");
		}

		return ResultData.from("S-1", "게시물 삭제가 가능합니다.");
	}

	public ResultData<Integer> writeArticle(int memberId, int boardId, String title, String body) {
		articleRepository.writeArticle(memberId, boardId, title, body);
		int id = articleRepository.getLastInsertId();
		return ResultData.from("S-1", Ut.f("%d번 게시물이 생성 되었습니다.", id), "id", id);
	}

	public void deleteArticle(int id) {
		articleRepository.deleteArticle(id);
	}

	public ResultData<Article> modifyArticle(int id, String title, String body) {
		articleRepository.modifyArticle(id, title, body);

		Article article = getForPrintArticle(0, id);

		return ResultData.from("S-1", Ut.f("%d번 게시물이 수정되었습니다.", id), "article", article);
	}

	// 로그인체크, 현제 접속한 id
	public ResultData actorCanModify(int actorId, Article article) {
		if (article == null) {
			return ResultData.from("F-1", "게시물이 존재하지않습니다.");
		}
		if (article.getMemberId() != actorId) {
			return ResultData.from("F-2", "권한이 없습니다.");
		}
		return ResultData.from("S-1", "수정이 가능합니다.");
	}

	public int getArticlesCount(int boardId, String searchKeywordTypeCode, String searchKeyword) {

		return articleRepository.getArticlesCount(boardId, searchKeywordTypeCode, searchKeyword);
	}

	public ResultData increaseHitCount(int id) {
		int affectedRowsCount = articleRepository.increaseHitCount(id);

		if (affectedRowsCount == 0) {
			return ResultData.from("F-1", "해당 게시물이 존재하지 않습니다.", "affectedRowsCount", affectedRowsCount);
		}

		return ResultData.from("S-1", "조회수가 증가되었다.", "affectedRowsCount", affectedRowsCount);
	}

	public int getArticleHitCount(int id) {
		return articleRepository.getArticleHitCount(id);
	}

	public ResultData increseGoodReactionPoint(int relId) {
		int affectedRowsCount = articleRepository.increseGoodReactionPoint(relId);

		if (affectedRowsCount == 0) {
			return ResultData.from("F-1", "해당 게시물이 존재하지 않습니다.", "affectedRowsCount", affectedRowsCount);
		}
		return ResultData.from("S-1", "좋아요 수가 증가되어습니다.", "affectedRowsCount", affectedRowsCount);
	}

	public ResultData increseBadReactionPoint(int relId) {
		int affectedRowsCount = articleRepository.increseBadReactionPoint(relId);

		if (affectedRowsCount == 0) {
			return ResultData.from("F-1", "해당 게시물이 존재하지 않습니다.", "affectedRowsCount", affectedRowsCount);
		}
		return ResultData.from("S-1", "싫어요 수가 증가되어습니다.", "affectedRowsCount", affectedRowsCount);
	}

	public ResultData decreseGoodReactionPoint(int relId) {
		int affectedRowsCount = articleRepository.decreseGoodReactionPoint(relId);

		if (affectedRowsCount == 0) {
			return ResultData.from("F-1", "해당 게시물이 존재하지 않습니다.", "affectedRowsCount", affectedRowsCount);
		}
		return ResultData.from("S-1", "좋아요 수가 감소되어습니다.", "affectedRowsCount", affectedRowsCount);
	}
	public ResultData decreseBadReactionPoint(int relId) {
		int affectedRowsCount = articleRepository.decreseBadReactionPoint(relId);

		if (affectedRowsCount == 0) {
			return ResultData.from("F-1", "해당 게시물이 존재하지 않습니다.", "affectedRowsCount", affectedRowsCount);
		}
		return ResultData.from("S-1", "싫어요 수가 감소되어습니다.", "affectedRowsCount", affectedRowsCount);
	}
	
}




