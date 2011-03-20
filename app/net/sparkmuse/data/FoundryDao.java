package net.sparkmuse.data;

import net.sparkmuse.data.paging.PageChangeRequest;
import net.sparkmuse.data.entity.Wish;

import java.util.List;

/**
 * @author neteller
 * @created: Mar 15, 2011
 */
public interface FoundryDao extends CrudDao {

  List<Wish> findRecentWishes(PageChangeRequest pageChangeRequest);

  List<Wish> findTaggedWishes(String tag, PageChangeRequest pageChangeRequest);

}
