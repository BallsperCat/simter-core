package tech.simter.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author RJ
 */
public class PageTest {
  @Test
  public void empty() {
    Page<Object> page = Page.build(0, 0, null, 0);
    assertThat(page.getNumber(), is(1));
    assertThat(page.getCapacity(), is(Page.DEFAULT_CAPACITY));
    assertThat(page.getRows(), notNullValue());
    assertThat(page.getTotalCount(), is(0L));

    assertThat(page.getPageCount(), is(0));
    assertThat(page.getSize(), is(0));
    assertThat(page.getOffset(), is(0));
    assertThat(page.isEmpty(), is(true));
    assertThat(page.isFirst(), is(true));
    assertThat(page.isLast(), is(true));
  }

  @Test
  public void firstPage() {
    List<String> items = new ArrayList<>();
    items.add("1");
    Page<String> page = Page.build(1, 25, items, 101);
    assertThat(page.getNumber(), is(1));
    assertThat(page.getCapacity(), is(25));
    assertThat(page.getRows(), notNullValue());
    assertThat(page.getTotalCount(), is(101L));

    assertThat(page.getPageCount(), is(5));
    assertThat(page.getSize(), is(1));
    assertThat(page.getOffset(), is(0));
    assertThat(page.isEmpty(), is(false));
    assertThat(page.isFirst(), is(true));
    assertThat(page.isLast(), is(false));
  }

  @Test
  public void secondPage() {
    List<String> items = new ArrayList<>();
    items.add("1");
    Page<String> page = Page.build(2, 25, items, 101);
    assertThat(page.getNumber(), is(2));
    assertThat(page.getCapacity(), is(25));
    assertThat(page.getRows(), notNullValue());
    assertThat(page.getTotalCount(), is(101L));

    assertThat(page.getPageCount(), is(5));
    assertThat(page.getSize(), is(1));
    assertThat(page.getOffset(), is(25));
    assertThat(page.isEmpty(), is(false));
    assertThat(page.isFirst(), is(false));
    assertThat(page.isLast(), is(false));

    // another
    page = Page.build(3, 25, items, 101);
    assertThat(page.getNumber(), is(3));
    assertThat(page.getCapacity(), is(25));
    assertThat(page.getRows(), notNullValue());
    assertThat(page.getTotalCount(), is(101L));

    assertThat(page.getPageCount(), is(5));
    assertThat(page.getSize(), is(1));
    assertThat(page.getOffset(), is(50));
    assertThat(page.isEmpty(), is(false));
    assertThat(page.isFirst(), is(false));
    assertThat(page.isLast(), is(false));
  }

  @Test
  public void lastPage() {
    List<String> items = new ArrayList<>();
    items.add("1");
    Page<String> page = Page.build(5, 25, items, 101);
    assertThat(page.getNumber(), is(5));
    assertThat(page.getCapacity(), is(25));
    assertThat(page.getRows(), notNullValue());
    assertThat(page.getTotalCount(), is(101L));

    assertThat(page.getPageCount(), is(5));
    assertThat(page.getSize(), is(1));
    assertThat(page.getOffset(), is(100));
    assertThat(page.isEmpty(), is(false));
    assertThat(page.isFirst(), is(false));
    assertThat(page.isLast(), is(true));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void canNotModifyPageData() {
    List<String> items = new ArrayList<>();
    items.add("1");
    Page<String> page = Page.build(1, 25, items, 101);
    page.getRows().add("something");
  }

  @Test
  public void toValidCapacity() {
    assertThat(Page.toValidCapacity(-1), is(Page.DEFAULT_CAPACITY));
    assertThat(Page.toValidCapacity(0), is(Page.DEFAULT_CAPACITY));
    assertThat(Page.toValidCapacity(1), is(1));
    assertThat(Page.toValidCapacity(10), is(10));
    assertThat(Page.toValidCapacity(100), is(100));
  }

  @Test
  public void calculateOffset() {
    assertThat(Page.calculateOffset(1, 1), is(0));
    assertThat(Page.calculateOffset(2, 1), is(1));

    assertThat(Page.calculateOffset(1, 25), is(0));
    assertThat(Page.calculateOffset(2, 25), is(25));

    assertThat(Page.calculateOffset(1, 100), is(0));
    assertThat(Page.calculateOffset(2, 100), is(100));

    assertThat(Page.calculateOffset(-1, -1), is(0));
    assertThat(Page.calculateOffset(-1, 0), is(0));
    assertThat(Page.calculateOffset(0, 0), is(0));
    assertThat(Page.calculateOffset(1, 0), is(0));
    assertThat(Page.calculateOffset(2, 0), is(25));
  }

  @Test
  public void iterator() {
    List<String> items = new ArrayList<>();
    for (int i = 0; i < 10; i++) items.add(String.valueOf(i));
    Page<String> page = Page.build(1, 25, items, 101);
    int i = 0;
    for (String row : page) {
      assertThat(row, is(String.valueOf(i)));
      i++;
    }
    //System.out.println(page.getClass());
    //System.out.println(new Genson().serialize(page));
  }
}
