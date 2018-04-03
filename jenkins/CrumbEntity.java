package com.weiqing.snarl.app.jenkins;

import lombok.Data;

import java.io.Serializable;

/**
 * @author miaoying
 * @date 4/2/18
 */
@Data
public class CrumbEntity implements Serializable {
    private String crumb;
    private String crumbRequestField;
}