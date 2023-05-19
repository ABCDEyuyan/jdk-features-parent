package com.nf.demo.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class PaginationText {
    private Pagination pagination;
    /**
     * 此参数确定首页附近的数字的数量，比如是个2的话，
     * 那么就应该1,2，如果值是3 --》1,2,3
     */
    private int firstCount = 2;
    private int lastCount = 2;
    private int prevCount = 2;
    private int nextCount = 2;
    private String skip = "...";

    public PaginationText(Pagination pagination) {
        this.pagination = pagination;
    }

    public PaginationText setFirstCount(int count) {
        this.firstCount = count;
        return this;
    }

    public PaginationText setLastCount(int count) {
        this.lastCount = count;
        return this;
    }

    public PaginationText setPrevCount(int count) {
        this.prevCount = count;
        return this;
    }

    public PaginationText setNextCount(int count) {
        this.nextCount = count;
        return this;
    }

    public PaginationText setSkip(String skipText) {
        this.skip = skipText;
        return this;
    }

    public Pagination getPagination() {
        return pagination;
    }

    private List<Integer> getPagedNumbers() {
        List<Integer> validNumbers = new ArrayList<>();
        List<Integer> listFirst = generate(1, firstCount);
        List<Integer> listPrev = generate(pagination.getPageNo() - prevCount + 1, prevCount);
        List<Integer> listNext = generate(pagination.getPageNo(), nextCount);
        List<Integer> listLast = generate(pagination.getLast() - lastCount + 1, lastCount);
        //把4组数据合并在一个集合里面。
        listFirst.addAll(listPrev);
        listFirst.addAll(listNext);
        listFirst.addAll(listLast);
        // 剔除掉越界的数据，比如-1 ，超过总页数的数据
        for (Integer pageNo : listFirst) {
            if (pageNo >= 1 && pageNo <= pagination.getTotalPages()) {
                validNumbers.add(pageNo);
            }
        }
        //剔除重复
        ArrayList<Integer> result = new ArrayList<>(new HashSet<>(validNumbers));
        //对数字进行从小到大的排序，如果上面的addAll方法是随意顺序调用，数字可能就是乱序的
        Collections.sort(result);
        return result;
    }

    public List<String> getPagedText() {
        List<String> result = new ArrayList<>();
        List<Integer> numbers = getPagedNumbers();
        for (int i = 0; i < numbers.size() - 1; i++) {
            int current = numbers.get(i);
            int next = numbers.get(i + 1);
            result.add(current + "");
            if ((next - current) > 1) {
                result.add(this.skip);
            }
        }
        result.add(numbers.get(numbers.size() - 1) + "");
        return result;
    }

    /**
     * 比如start = 5 ，count = 0--》一个数字都不生成
     * start=5 ,count = 1----> 5
     * start=5,count =2 ---> 5,6
     * start=5,count=3 ---->5,6,7
     *
     * @param start:表示从哪个数字开始生成数字,必须大于等于1
     * @param count：生成数字的总数量，大于等于0
     * @return
     */
    private List<Integer> generate(int start, int count) {
        List<Integer> result = new ArrayList<>();

        for (int i = start; i < (start + count); i++) {
            result.add(i);
        }
        return result;
    }

    public String getSkip() {
        return skip;
    }

    public static void main(String[] args) {
        Pagination pagination = new Pagination(1, 10L);

        PaginationText paginationText = new PaginationText(pagination);
        List<String> pagedText = paginationText.getPagedText();
        for (String s : pagedText) {
            System.out.println("s = " + s);
        }
    }
}
