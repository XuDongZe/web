package com.xdz.leetcode.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//leetcode submit region begin(Prohibit modification and deletion)
class ThroneInheritance {

    // parent -> children 多叉树表示
    private String king;
    private Map<String, List<String>> childrenMap;
    private Set<String> death;

    public ThroneInheritance(String kingName) {
        king = kingName;
        childrenMap = new HashMap<>();
        death = new HashSet<>();
    }

    public void birth(String parentName, String childName) {
        List<String> list = childrenMap.computeIfAbsent(parentName, k -> new ArrayList<>());
        list.add(childName);
        childrenMap.put(parentName, list);
    }
    
    public void death(String name) {
        death.add(name);
    }
    
    public List<String> getInheritanceOrder() {
        List<String> ans = new ArrayList<>();
        preOrder(king, ans);
        return ans;
    }

    private void preOrder(String name, List<String> ans) {
        if (!death.contains(name)) {
            ans.add(name);
        }
        List<String> list = childrenMap.get(name);
        if (list == null) {
            return;
        }
        for (String c : list) {
            preOrder(c, ans);
        }
    }
}

/**
 * Your ThroneInheritance object will be instantiated and called as such:
 * ThroneInheritance obj = new ThroneInheritance(kingName);
 * obj.birth(parentName,childName);
 * obj.death(name);
 * List<String> param_3 = obj.getInheritanceOrder();
 */
//leetcode submit region end(Prohibit modification and deletion)
