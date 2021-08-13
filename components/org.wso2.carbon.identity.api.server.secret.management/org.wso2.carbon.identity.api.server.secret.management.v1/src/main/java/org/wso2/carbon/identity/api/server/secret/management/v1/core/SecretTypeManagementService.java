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

package org.wso2.carbon.identity.api.server.secret.management.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants;
import org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementServiceHolder;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeAddRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeResponse;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeUpdateRequest;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementClientException;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementException;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementServerException;
import org.wso2.carbon.identity.secret.mgt.core.model.SecretType;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.CONFIG_MGT_ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.ERROR_PREFIX;

/**
 * Invoke internal OSGi service to perform secret type management operations.
 */
public class SecretTypeManagementService {

    private static final Log log = LogFactory.getLog(SecretManagementService.class);

    /**
     * Create a secret Type.
     *
     * @param secretTypeAddRequest secret post request.
     * @return secret.
     */
    public SecretTypeResponse addSecretType(SecretTypeAddRequest secretTypeAddRequest) {

        validateSecretTypeAddRequest(secretTypeAddRequest);
        SecretType requestDTO, responseDTO;
        try {
            requestDTO = buildSecretTypeRequestDTOFromSecretTypeAddRequest(secretTypeAddRequest);
            responseDTO = SecretManagementServiceHolder.getSecretConfigManager().addSecretType(requestDTO);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_SECRET,
                    secretTypeAddRequest.getName());
        }
        return buildSecretTypeResponseFromResponseDTO(responseDTO);
    }

    /**
     * To create Secret Response object for the post request
     *
     * @param secretTypeReq secret object.
     * @return {@link SecretTypeResponse} .
     */
    private SecretTypeResponse buildSecretTypeResponseFromResponseDTO(SecretType secretTypeReq) {

        SecretTypeResponse secretTypeResponse = new SecretTypeResponse();
        secretTypeResponse.setId(secretTypeReq.getId());
        secretTypeResponse.setName(secretTypeReq.getName());
        secretTypeResponse.setDescription(secretTypeReq.getDescription());
        return secretTypeResponse;
    }

    /**
     * Validate the secret post request.
     *
     * @param secretTypeAddRequest secret post request.
     */
    private void validateSecretTypeAddRequest(SecretTypeAddRequest secretTypeAddRequest) {

        String secretAddName = secretTypeAddRequest.getName();
        if (StringUtils.isBlank(secretAddName)) {
            throw handleException(Response.Status.BAD_REQUEST, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_REFERENCE_NAME_NOT_SPECIFIED, null);
        }
    }

    /**
     * Build secret requestDTO by secret body request.
     *
     * @param secretTypeAddRequest secret type post body.
     * @return Secret requestDTO object.
     */
    private SecretType buildSecretTypeRequestDTOFromSecretTypeAddRequest(SecretTypeAddRequest secretTypeAddRequest) {

        SecretType requestDTO = new SecretType();
        requestDTO.setName(secretTypeAddRequest.getName());
        requestDTO.setDescription(secretTypeAddRequest.getDescription());
        return requestDTO;
    }

    /**
     * Delete a secret type by name.
     *
     * @param secretTypeName Name of the secret type.
     */
    public void deleteSecretType(String secretTypeName) {

        try {
            SecretManagementServiceHolder.getSecretConfigManager().deleteSecretType(secretTypeName);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_DELETING_SECRET, secretTypeName);
        }
    }

    /**
     * Retrieve the secret details by name.
     *
     * @param secretTypeName secret name.
     * @return secret.
     */
    public SecretTypeResponse getSecretType(String secretTypeName) {

        try {

            SecretType responseDTO = SecretManagementServiceHolder.getSecretConfigManager().getSecretType
                    (secretTypeName);
            SecretTypeResponse secretTypeResponse = new SecretTypeResponse();
            secretTypeResponse.setId(responseDTO.getId());
            secretTypeResponse.setName(responseDTO.getName());
            secretTypeResponse.description(responseDTO.getDescription());
            return secretTypeResponse;
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_GETTING_SECRET, secretTypeName);
        }
    }

    /**
     * Update secret details by name.
     *
     * @param name                secret name.
     * @param secretUpdateRequest secret's updated details.
     * @return Updated secret.
     */
    public SecretTypeResponse updateTypeSecret(String name, SecretTypeUpdateRequest secretUpdateRequest) {

        SecretType requestDTO, responseDTO;
        SecretTypeAddRequest secretAdd = buildSecretTypeAddFromSecretTypeUpdateRequest(name, secretUpdateRequest);
        try {
            requestDTO = buildSecretTypeRequestDTOFromSecretTypeAddRequest(secretAdd);
            responseDTO = SecretManagementServiceHolder.getSecretConfigManager().replaceSecretType(requestDTO);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SECRET,
                    name);
        }
        return buildSecretTypeResponseFromResponseDTO(responseDTO);
    }

    /**
     * Build secretAdd object from secret type update request.
     *
     * @param name                    secret type name.
     * @param secretTypeUpdateRequest secret type's update request body.
     * @return secretTypeAdd object
     */
    private SecretTypeAddRequest buildSecretTypeAddFromSecretTypeUpdateRequest(String name, SecretTypeUpdateRequest
            secretTypeUpdateRequest) {

        SecretTypeAddRequest secretTypeAdd = new SecretTypeAddRequest();
        secretTypeAdd.setName(name);
        secretTypeAdd.setDescription(secretTypeUpdateRequest.getDescription());
        return secretTypeAdd;
    }

    private APIError handleSecretMgtException(SecretManagementException e, SecretManagementConstants.ErrorMessage
            errorEnum, String data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
        Response.Status status;
        if (e instanceof SecretManagementClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(CONFIG_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof SecretManagementServerException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(CONFIG_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, SecretManagementConstants.ErrorMessage error,
                                     String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(SecretManagementConstants.ErrorMessage errorMsg,
                                                  String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Error message.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private static String includeData(SecretManagementConstants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getDescription(), data);
        } else {
            message = error.getDescription();
        }
        return message;
    }
}
