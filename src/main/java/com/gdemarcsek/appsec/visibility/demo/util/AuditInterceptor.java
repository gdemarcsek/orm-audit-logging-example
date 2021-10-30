package com.gdemarcsek.appsec.visibility.demo.util;

import com.gdemarcsek.appsec.visibility.demo.core.EntityBase;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import java.util.Optional;
import java.io.Serializable;


@Slf4j
public class AuditInterceptor extends EmptyInterceptor {
    private void updateMDC(Object entity) {
        MDC.put("entityWriteCount",
                String.valueOf(Integer.parseInt(Optional.ofNullable(MDC.get("updatedEntityCount")).orElse("0")) + 1));
        MDC.put("writtenEntityId", ((EntityBase) entity).getId().toString());
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types) {
        this.updateMDC(entity);
        log.info("entity updated");
        return false;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        this.updateMDC(entity);
        log.info("new entity persisted");
        return false;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        this.updateMDC(entity);
        log.info("entity deleted");
    }
}