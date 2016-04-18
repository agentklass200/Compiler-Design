package Parsing;

import java.util.List;
import java.util.ArrayList;

public class TreeNode<T> {
	private T data;
	private List<TreeNode<T>> children;
	private TreeNode<T> parent;
}
