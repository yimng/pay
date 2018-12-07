package com.edupay.cache.redis.utils;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

/**
 * com.caicui.redis.cache.redis.utils
 * Created by yukewi on 2015/12/16 16:59.
 */
public final class NameUtils {
    public static final NameUtils INSTANCE = new NameUtils();

    private NameUtils() {
    }

    public static NameUtils getInstance() {
        return INSTANCE;
    }

    /**
     * 主函数入口方法
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        List<String> list = new LinkedList<>();
        list.add("c.c.s.l.d.M.searchMemberCapabilityList");
        list.add("c.c.s.l.d.M.searchMemberCapabilityList");
        list.add("org.mybatis.spring.SqlSessionUtils");
        list.add("o.s.d.r.core.RedisConnectionUtils");
        list.add("o.s.d.r.core.RedisConnectionUtils");
        list.add("o.s.d.r.core.RedisConnectionUtils");
        list.add("o.s.d.r.core.RedisConnectionUtils");
        list.add("o.s.d.r.core.RedisConnectionUtils");
        list.add("o.s.d.r.core.RedisConnectionUtils");
        list.add("o.s.j.d.DataSourceTransactionManager");
        list.add("o.s.j.d.DataSourceTransactionManager");
        list.add("org.mybatis.spring.SqlSessionUtils");
        list.add("org.mybatis.spring.SqlSessionUtils");
        list.add("o.s.j.d.DataSourceTransactionManager");
        list.add("o.s.jdbc.datasource.DataSourceUtils");
        list.add("o.s.j.d.DataSourceTransactionManager");
        list.add("o.s.j.d.DataSourceTransactionManager");
        list.add("o.s.d.r.core.RedisConnectionUtils");
        list.add("o.s.d.r.core.RedisConnectionUtils");
        list.add("o.s.j.d.DataSourceTransactionManager");
        list.add("o.s.j.d.DataSourceTransactionManager");
        list.add("o.s.j.d.DataSourceTransactionManager");
        list.add("o.s.jdbc.datasource.DataSourceUtils");
        list.add("com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper.invokeProtocolFilterWrapper");
        list.add("com.alibaba.dubbo.monitor.support.MonitorFilter.invoke.MonitorFilter");
        list.add("com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper.invokeProtocolFilterWrapper");
        list.add("com.alibaba.dubbo.rpc.filter.ConsumerContextFilter.invoke.ConsumerContextFilter");
        list.add("com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper.invokeProtocolFilterWrapper");
        list.add("com.alibaba.dubbo.rpc.protocol.InvokerWrapper.invoke.InvokerWrapper");
        list.add("com.alibaba.dubbo.rpc.cluster.support.FailoverClusterInvoker.doInvoke.FailoverClusterInvoker");
        list.add("com.alibaba.dubbo.rpc.cluster.support.FailoverClusterInvoker.doInvoke");
        list.add("com.alibaba.dubbo.rpc.cluster.support.AbstractClusterInvoker.invoke");
        list.add("com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker.invoke");
        list.add("com.alibaba.dubbo.rpc.proxy.InvokerInvocionHandler.invoke");
        list.add("com.alibaba.dubbo.common.bytecode.proxy0.getCapabilityAssessment");
        list.add("com.bitzh.controller.api.CapabilityAssessmentController.execute");
        list.add("sun.reflect.GeneredMethodAccessor541.invoke");
        list.add("sun.reflect.DelegingMethodAccessorImpl.invoke");
        list.add("java.lang.reflect.Method.invoke");
        list.add("org.springframework.web.bind.annotion.support.HandlerMethodInvoker.invokeHandlerMethod");
        list.add("org.springframework.web.servlet.mvc.annotion.AnnotionMethodHandlerAdapter.invokeHandlerMethod");
        list.add("org.springframework.web.servlet.mvc.annotion.AnnotionMethodHandlerAdapter.handle");
        list.add("org.springframework.web.servlet.DispcherServlet.doDispch");
        list.add("org.springframework.web.servlet.DispcherServlet.doService");
        list.add("org.springframework.web.servlet.FrameworkServlet.processRequest");
        list.add("org.springframework.web.servlet.FrameworkServlet.doGet");
        list.add("javax.servlet.http.HttpServlet.service");
        list.add("javax.servlet.http.HttpServlet.service");
        list.add("org.apache.calina.core.ApplicionFilterChain.internalDoFilter");
        list.add("org.apache.calina.core.ApplicionFilterChain.doFilter");
        list.add("org.apache.tomc.websocket.server.WsFilter.doFilter");
        list.add("org.apache.calina.core.ApplicionFilterChain.internalDoFilter");
        list.add("org.apache.calina.core.ApplicionFilterChain.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy$VirtualFilterChain.doFilter");
        list.add("org.springframework.security.intercept.web.FilterSecurityInterceptor.invoke");
        list.add("org.springframework.security.intercept.web.FilterSecurityInterceptor.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy$VirtualFilterChain.doFilter");
        list.add("org.springframework.security.ui.SessionFixionProtectionFilter.doFilterHttp");
        list.add("org.springframework.security.ui.SpringSecurityFilter.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy$VirtualFilterChain.doFilter");
        list.add("org.springframework.security.ui.ExceptionTranslionFilter.doFilterHttp");
        list.add("org.springframework.security.ui.SpringSecurityFilter.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy$VirtualFilterChain.doFilter");
        list.add("org.springframework.security.providers.anonymous.AnonymousProcessingFilter.doFilterHttp");
        list.add("org.springframework.security.ui.SpringSecurityFilter.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy$VirtualFilterChain.doFilter");
        list.add("org.springframework.security.ui.rememberme.RememberMeProcessingFilter.doFilterHttp");
        list.add("org.springframework.security.ui.SpringSecurityFilter.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy$VirtualFilterChain.doFilter");
        list.add("org.springframework.security.ui.basicauth.BasicProcessingFilter.doFilterHttp");
        list.add("org.springframework.security.ui.SpringSecurityFilter.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy$VirtualFilterChain.doFilter");
        list.add("org.springframework.security.ui.AbstractProcessingFilter.doFilterHttp");
        list.add("org.springframework.security.ui.SpringSecurityFilter.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy$VirtualFilterChain.doFilter");
        list.add("org.springframework.security.ui.logout.LogoutFilter.doFilterHttp");
        list.add("org.springframework.security.ui.SpringSecurityFilter.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy$VirtualFilterChain.doFilter");
        list.add("org.springframework.security.context.HttpSessionContextIntegrionFilter.doFilterHttp");
        list.add("org.springframework.security.ui.SpringSecurityFilter.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy$VirtualFilterChain.doFilter");
        list.add("org.springframework.security.util.FilterChainProxy.doFilter");
        list.add("org.springframework.web.filter.DelegingFilterProxy.invokeDelege");
        list.add("org.springframework.web.filter.DelegingFilterProxy.doFilter");
        list.add("org.apache.calina.core.ApplicionFilterChain.internalDoFilter");
        list.add("org.apache.calina.core.ApplicionFilterChain.doFilter");
        list.add("org.springframework.orm.hiberne3.support.OpenSessionInViewFilter.doFilterInternal");
        list.add("org.springframework.web.filter.OncePerRequestFilter.doFilter");
        list.add("org.apache.calina.core.ApplicionFilterChain.internalDoFilter");
        list.add("org.apache.calina.core.ApplicionFilterChain.doFilter");
        list.add("org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal");
        list.add("org.springframework.web.filter.OncePerRequestFilter.doFilter");
        list.add("org.apache.calina.core.ApplicionFilterChain.internalDoFilter");
        list.add("org.apache.calina.core.ApplicionFilterChain.doFilter");
        list.add("org.apache.calina.core.StandardWrapperValve.invoke");
        list.add("org.apache.calina.core.StandardContextValve.invoke");
        list.add("org.apache.calina.authenticor.AuthenticorBase.invoke");
        list.add("org.apache.calina.core.StandardHostValve.invoke");
        list.add("org.apache.calina.valves.ErrorReportValve.invoke");
        list.add("org.apache.calina.valves.AccessLogValve.invoke");
        list.add("org.apache.calina.core.StandardEngineValve.invoke");
        list.add("org.apache.calina.connector.CoyoteAdapter.service");
        list.add("org.apache.coyote.http11.AbstractHttp11Processor.process");
        list.add("org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process");
        list.add("org.apache.tomc.util.net.JIoEndpoint$SocketProcessor.run");
        list.add("java.util.concurrent.ThreadPoolExecutor.runWorker");
        list.add("java.util.concurrent.ThreadPoolExecutor$Worker.run");
        list.add("org.apache.tomc.util.threads.TaskThread$Wrappinibaba.dubbo.remoting.exchange.support.DefaultFuture.get");
        list.add("com.alibaba.dubbo.remoting.exchange.support.DefaultFuture.get");
        list.add("com.alibaba.dubbo.rpc.protocol.dubbo.DubboInvoker.doInvoke");
        list.add("com.alibaba.dubbo.rpc.protocol.AbstractInvoker.invoke");
        list.add("com.alibaba.dubbo.rpc.listener.ListenerInvokerWrapper.invoke");
        list.add("com.alibaba.dubbo.rpc.protocol.dubbo.filter.FutureFilter.invoke");
        list.add("com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke");
        list.add("com.alibaba.dubbo.monitor.support.MonitorFilter.invoke");
        list.add("com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke");
        list.add("com.alibaba.dubbo.rpc.filter.ConsumerContextFilter.invoke");
        list.add("com.alibaba.dubbo.rpc.protocol.ProtocolFilterWrapper$1.invoke");
        list.add("com.alibaba.dubbo.rpc.protocol.InvokerWrapper.invoke");
        list.add("com.alibaba.dubbo.rpc.cluster.support.FailoverClusterInvoker.doInvoke");
        final List<Map<String, Object>> list1 = getInstance().toNodeMap(list);
        for (Map<String, Object> map : list1) {
            System.out.println(map);
        }

    }

    public List<Map<String, Object>> toNodeMap(Collection<String> nameList) {
        List<Map<String, Object>> mapList = new LinkedList<>();
        final List<Node> nodes = toNodes(nameList);
        for (Node node : nodes) {
            mapList.add(node.toNodeMap());
        }
        return mapList;
    }

    private List<String> split(String name) {
        List<String> list = new LinkedList<>();
        if (StringUtils.isNotEmpty(name)) {
            final String[] split = StringUtils.split(name, ".$#");
            list.addAll(Arrays.asList(split));
        }
        return list;
    }

    private List<Node> toNodes(Collection<String> nameList) {
        nameList = unique(nameList);
        Set<Node> nodeSet = new LinkedHashSet<>();
        for (String name : nameList) {
            final List<String> split = split(name);
            final List<Node> nodes = toNodes(split, name);
            nodeSet.addAll(nodes);
        }
        List<Node> nodeList = new LinkedList<>();
        nodeList.addAll(nodeSet);
        return nodeList;
    }

    private List<Node> toNodes(List<String> splits, String rawName) {
        List<Node> nodes = new LinkedList<>();
        for (int i = 0; i < splits.size(); i++) {
            final String name = splits.get(i);
            final List<String> list = splits.subList(0, i + 1);
            final String id = StringUtils.join(list, ".");

            Node node = new Node(id, name);
            node.setIsLeaf(false);
            if (i != 0) {
                final List<String> pList = splits.subList(0, i);
                String pId = StringUtils.join(pList, ".");
                node.setpId(pId);
            }
            if (i == (splits.size() - 1)) {
                node.setIsLeaf(true);
                node.setRaw(rawName);
            }
            nodes.add(node);
        }
        return nodes;
    }

    private List<String> unique(Collection<String> nameList) {
        List<String> list = new LinkedList<>();
        if (CollectionUtils.isNotEmpty(nameList)) {
            Set<String> set = new LinkedHashSet<>();
            set.addAll(nameList);
            list.addAll(set);
            Collections.sort(list);
        }
        return list;
    }


}


class Node {
    private String id;
    private String name;
    private String pId;
    private Boolean isLeaf;
    private String raw;

    public Node(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public String getName() {
        return name;
    }

    public String getRaw() {
        return raw;
    }

    public String getpId() {
        return pId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return new EqualsBuilder()
                .append(getId(), node.getId())
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public Map<String, Object> toNodeMap() {
        String key = raw;
        Boolean open = false;
        if (pId == null) {
            open = true;
        }

        if (pId != null && !isLeaf) {
            open = true;
        }
        return getNodeMap(id, pId, name, isLeaf, key, open);
    }

    /**
     * 组织节点Map对象
     *
     * @param id
     * @param pId
     * @param name
     * @param isLeaf
     * @param key
     * @param open
     * @return
     */
    private Map<String, Object> getNodeMap(String id, String pId, String name, Boolean isLeaf, String key, Boolean open) {
        Map<String, Object> nodeMap = new HashMap<String, Object>();
        nodeMap.put("id", id);
        nodeMap.put("pId", pId);
        nodeMap.put("name", name);
        nodeMap.put("isLeaf", isLeaf);
        nodeMap.put("key", key);
        nodeMap.put("open", open);
        return nodeMap;
    }
}
