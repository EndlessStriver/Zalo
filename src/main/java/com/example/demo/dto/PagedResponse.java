package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
	private List<T> data;
	private int currentPage;
	private int totalPages;
	private int pageSize;
	private int totalItems;
}
