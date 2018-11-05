package com.scs.framework.core.dao.intercepts;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scs.framework.core.web.context.RequestHeaderUtils;

/**
 * 数据权限拦截器
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月17日
 */

@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class,
    Object.class, RowBounds.class, ResultHandler.class }) })
public class OriginInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(OriginInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (logger.isInfoEnabled()) {
            logger.info("进入 OriginIntercepts 拦截器...");
        }
        invocation.getMethod();
        invocation.getArgs();
        RequestHeaderUtils.getUserId();

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String originalSql = boundSql.getSql().trim();

        BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, originalSql);
        ParameterMap map = mappedStatement.getParameterMap();
        // ParameterMapping mapping=new
        // ParameterMapping.Builder(mappedStatement.getConfiguration(), check_user,
        // String.class).build();
        // map.getParameterMappings().add(mapping);
        MappedStatement newMs = copyFromMappedStatement(mappedStatement,
                new BoundSqlSqlSource(newBoundSql), map);
        invocation.getArgs()[0] = newMs;

        return invocation.proceed();
    }

    public class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    /**
     * 复制MappedStatement对象
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource,
                                                    ParameterMap parameterMap) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
                ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        // builder.keyProperty(ms.getKeyProperty());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(parameterMap);
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    /**
     * 复制BoundSql对象
     */
    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql,
                boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
