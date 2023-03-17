package me.mrabar.sq.component;

import me.mrabar.sq.model.PageOverviewComments;
import me.mrabar.sq.model.Pair;

import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class BlogComponent {

  public List<Pair<Timestamp, List<PageOverviewComments>>> groupByWeek(List<PageOverviewComments> rows) {
    Timestamp ts = null;
    List<PageOverviewComments> group = new ArrayList<>();
    List<Pair<Timestamp, List<PageOverviewComments>>> result = new ArrayList<>();
    for (PageOverviewComments row : rows) {
      if (!row.weekNumber().equals(ts)) {
        if (!group.isEmpty() && ts != null) {
          result.add(new Pair<>(ts, group));
        }
        ts = row.weekNumber();
        group = new ArrayList<>();
      }

      group.add(row);
    }

    if (!group.isEmpty() && ts != null) {
      result.add(new Pair<>(ts, group));
    }

    return result;
  }
}
