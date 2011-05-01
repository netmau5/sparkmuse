package net.sparkmuse.data;

import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.data.entity.Wish;
import net.sparkmuse.data.entity.Comment;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.Commitment;

import java.util.List;

/**
 * @author neteller
 * @created: Mar 15, 2011
 */
public interface FoundryDao extends CrudDao {

  List<Wish> findRecentWishes(PageChangeRequest pageChangeRequest);

  List<Wish> findPopularWishes(PageChangeRequest request);

  List<Wish> findTaggedWishes(String tag, PageChangeRequest pageChangeRequest);

  List<Comment> findWishCommentsBy(Long wishId);

  List<Commitment> findCommitmentsFor(Long requestingUserId, Long wishId);

  List<Wish> findTopWishes();
}
