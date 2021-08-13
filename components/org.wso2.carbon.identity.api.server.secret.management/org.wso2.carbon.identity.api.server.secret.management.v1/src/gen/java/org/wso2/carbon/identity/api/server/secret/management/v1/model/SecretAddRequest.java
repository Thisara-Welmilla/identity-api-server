/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.secret.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

import java.util.Objects;
import javax.validation.Valid;

public class SecretAddRequest {

    private String name;
    private String value;
    private String description;

    /**
     *
     **/
    public SecretAddRequest name(String name) {

        this.name = name;
        return this;
    }

    @ApiModelProperty(example = "choreo-riskScore", required = true, value = "")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    /**
     *
     **/
    public SecretAddRequest value(String value) {

        this.value = value;
        return this;
    }

    @ApiModelProperty(example = "bgtehaldhjdevch", required = true, value = "")
    @JsonProperty("value")
    @Valid
    @NotNull(message = "Property value cannot be null.")

    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    /**
     *
     **/
    public SecretAddRequest description(String description) {

        this.description = description;
        return this;
    }

    @ApiModelProperty(example = "Some Error Description", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecretAddRequest secretAddRequest = (SecretAddRequest) o;
        return Objects.equals(this.name, secretAddRequest.name) &&
                Objects.equals(this.value, secretAddRequest.value) &&
                Objects.equals(this.description, secretAddRequest.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, value, description);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SecretAddRequest {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}
