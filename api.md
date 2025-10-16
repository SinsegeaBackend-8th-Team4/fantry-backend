{
  "openapi": "3.1.0",
  "info": {
    "title": "OpenAPI definition",
    "version": "v0"
  },
  "servers": [
    {
      "url": "http://localhost:8080",
      "description": "Generated server url"
    }
  ],
  "tags": [
    {
      "name": "Notice (Admin)",
      "description": "관리자 공지사항 관리 API"
    },
    {
      "name": "user-inspection-controller",
      "description": "유저용 검수 컨트롤러"
    },
    {
      "name": "Inquiry (User)",
      "description": "사용자 1:1 문의 API"
    },
    {
      "name": "Admin Settlement Management",
      "description": "관리자 정산 관련 API"
    },
    {
      "name": "Inquiry (Admin)",
      "description": "관리자 1:1 문의 관리 API"
    },
    {
      "name": "Admin Dashboard Management",
      "description": "관리자 대시보드 관련 API"
    },
    {
      "name": "FAQ (User)",
      "description": "사용자용 FAQ API"
    },
    {
      "name": "FAQ (Admin)",
      "description": "관리자용 FAQ API"
    },
    {
      "name": "My Settlement",
      "description": "판매자 정산 내역 조회 API"
    },
    {
      "name": "admin-inspection-controller",
      "description": "관리자용 컨트롤러"
    },
    {
      "name": "Refund/Return (Admin)",
      "description": "관리자 환불/반품 관리 API"
    },
    {
      "name": "Refund/Return (User)",
      "description": "사용자 환불/반품 API"
    }
  ],
  "paths": {
    "/api/role/{id}": {
      "get": {
        "tags": [
          "role-controller"
        ],
        "operationId": "getRoleById",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "role-controller"
        ],
        "operationId": "updateRole",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/Role"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "role-controller"
        ],
        "operationId": "deleteRole",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/report/{reportId}": {
      "get": {
        "tags": [
          "report-controller"
        ],
        "operationId": "getReport",
        "parameters": [
          {
            "name": "reportId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "report-controller"
        ],
        "operationId": "updateReport",
        "parameters": [
          {
            "name": "reportId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ReportRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "report-controller"
        ],
        "operationId": "deleteReport",
        "parameters": [
          {
            "name": "reportId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/member/{id}": {
      "get": {
        "tags": [
          "member-controller"
        ],
        "operationId": "getMemberById",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "member-controller"
        ],
        "operationId": "updateMember",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/MemberUpdateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "member-controller"
        ],
        "operationId": "deleteMember",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/member/{id}/role": {
      "put": {
        "tags": [
          "member-controller"
        ],
        "operationId": "updateMemberRole",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/Role"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/member/{id}/delete": {
      "put": {
        "tags": [
          "member-controller"
        ],
        "operationId": "deleteMemberById",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/address/{addressId}": {
      "get": {
        "tags": [
          "address-controller"
        ],
        "operationId": "getAddress",
        "parameters": [
          {
            "name": "addressId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "address-controller"
        ],
        "operationId": "updateAddress",
        "parameters": [
          {
            "name": "addressId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/AddressRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "address-controller"
        ],
        "operationId": "deleteAddress",
        "parameters": [
          {
            "name": "addressId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/account/{accountId}": {
      "get": {
        "tags": [
          "account-controller"
        ],
        "operationId": "getAccount",
        "parameters": [
          {
            "name": "accountId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "account-controller"
        ],
        "operationId": "updateAccount",
        "parameters": [
          {
            "name": "accountId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/AccountRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "account-controller"
        ],
        "operationId": "deleteAccount",
        "parameters": [
          {
            "name": "accountId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/{type}/signup": {
      "post": {
        "tags": [
          "signup-controller"
        ],
        "operationId": "signup",
        "parameters": [
          {
            "name": "type",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/MemberDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/user/verifyCode": {
      "post": {
        "tags": [
          "signup-controller"
        ],
        "operationId": "verifyCode",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/AuthCodeDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/send/welcome": {
      "post": {
        "tags": [
          "mail-controller"
        ],
        "operationId": "sendWelcome",
        "parameters": [
          {
            "name": "to",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/send/reset-password": {
      "post": {
        "tags": [
          "mail-controller"
        ],
        "operationId": "sendPasswordResetEmail",
        "parameters": [
          {
            "name": "to",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "resetLink",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/send/mail": {
      "post": {
        "tags": [
          "mail-controller"
        ],
        "operationId": "sendMailForm",
        "parameters": [
          {
            "name": "to",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "subject",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "text",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "link",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "linkTitle",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/send/mail/json": {
      "post": {
        "tags": [
          "mail-controller"
        ],
        "operationId": "sendMailJsonForm",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/MailRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/send/authCode": {
      "post": {
        "tags": [
          "mail-controller"
        ],
        "operationId": "getCode",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/MemberDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/role/": {
      "get": {
        "tags": [
          "role-controller"
        ],
        "operationId": "getRole",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "role-controller"
        ],
        "operationId": "addRole",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/Role"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/returns": {
      "get": {
        "tags": [
          "Refund/Return (User)"
        ],
        "summary": "나의 환불/반품 요청 목록 조회",
        "description": "현재 로그인한 사용자의 환불/반품 요청 목록을 페이징하여 조회합니다.",
        "operationId": "getMyReturnRequests",
        "parameters": [
          {
            "name": "pageable",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          },
          {
            "name": "page",
            "in": "query",
            "description": "페이지 번호 (0부터 시작)",
            "example": 0
          },
          {
            "name": "size",
            "in": "query",
            "description": "페이지 당 항목 수",
            "example": 10
          },
          {
            "name": "sort",
            "in": "query",
            "description": "정렬 조건 (예: createdAt,desc)"
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageReturnSummaryResponse"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Refund/Return (User)"
        ],
        "summary": "환불/반품 요청 생성",
        "description": "새로운 환불/반품을 요청합니다.",
        "operationId": "createReturnRequest",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ReturnCreateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ReturnDetailResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/returns/{returnRequestId}/attachments": {
      "post": {
        "tags": [
          "Refund/Return (User)"
        ],
        "summary": "환불/반품 증빙 자료 첨부",
        "description": "특정 환불/반품 요청에 증빙 자료(파일)를 첨부합니다.",
        "operationId": "addAttachments",
        "parameters": [
          {
            "name": "returnRequestId",
            "in": "path",
            "description": "파일을 첨부할 요청 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "multipart/form-data": {
              "schema": {
                "type": "object",
                "properties": {
                  "files": {
                    "type": "array",
                    "description": "첨부할 파일 목록",
                    "items": {
                      "type": "string",
                      "format": "binary"
                    }
                  }
                },
                "required": [
                  "files"
                ]
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "OK"
          }
        }
      }
    },
    "/api/report/": {
      "get": {
        "tags": [
          "report-controller"
        ],
        "operationId": "getReports",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "report-controller"
        ],
        "operationId": "createReport",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ReportRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/reissue": {
      "post": {
        "tags": [
          "login-controller"
        ],
        "operationId": "reissue",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/pricing/estimate": {
      "post": {
        "tags": [
          "pricing-controller"
        ],
        "operationId": "estimate",
        "parameters": [
          {
            "name": "goodsCategoryId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32",
              "exclusiveMinimum": 0
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "additionalProperties": {
                  "type": "string"
                }
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseDouble"
                }
              }
            }
          }
        }
      }
    },
    "/api/payments": {
      "post": {
        "tags": [
          "payment-controller"
        ],
        "operationId": "requestPaymentCreate",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/PaymentCreateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ApiResponsePaymentResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/payments/{orderId}/cancel": {
      "post": {
        "tags": [
          "payment-controller"
        ],
        "operationId": "requestPaymentCancel",
        "parameters": [
          {
            "name": "orderId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/PaymentCancelRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ApiResponsePaymentCancelResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/payments/{orderId}/approve": {
      "post": {
        "tags": [
          "payment-controller"
        ],
        "operationId": "requestPaymentApprove",
        "parameters": [
          {
            "name": "orderId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/PaymentApproveRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ApiResponseString"
                }
              }
            }
          }
        }
      }
    },
    "/api/member/": {
      "get": {
        "tags": [
          "member-controller"
        ],
        "operationId": "getMembers",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "member-controller"
        ],
        "operationId": "addMember",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/MemberCreateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/logout": {
      "post": {
        "tags": [
          "login-controller"
        ],
        "operationId": "logout",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/login": {
      "post": {
        "tags": [
          "login-controller"
        ],
        "operationId": "login",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/MemberDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/inspections": {
      "post": {
        "tags": [
          "user-inspection-controller"
        ],
        "summary": "1차 검수 신청 생성",
        "description": "1차 검수 신청 생성",
        "operationId": "createInspection",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": {
                  "request": {
                    "$ref": "#/components/schemas/InspectionRequest"
                  },
                  "files": {
                    "type": "array",
                    "items": {
                      "type": "string",
                      "format": "binary"
                    }
                  }
                },
                "required": [
                  "files",
                  "request"
                ]
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseInteger"
                }
              }
            }
          }
        }
      }
    },
    "/api/inspections/{productInspectionId}/start-offline": {
      "post": {
        "tags": [
          "user-inspection-controller"
        ],
        "summary": "1차 승인된 상품의 발송을 확인하고, 2차 검수 상태 변경",
        "description": "1차 승인된 상품의 발송을 확인하고, 2차 검수 상태 변경",
        "operationId": "startOffline",
        "parameters": [
          {
            "name": "productInspectionId",
            "in": "path",
            "description": "상태를 변경할 검수 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "성공 응답",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseVoid"
                }
              }
            }
          }
        }
      }
    },
    "/api/file/upload": {
      "post": {
        "tags": [
          "test-controller"
        ],
        "operationId": "uploadFiles",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": {
                  "files": {
                    "type": "array",
                    "items": {
                      "type": "string",
                      "format": "binary"
                    }
                  }
                },
                "required": [
                  "files"
                ]
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/FileMeta"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/api/cs/inquiry": {
      "get": {
        "tags": [
          "Inquiry (User)"
        ],
        "summary": "나의 1:1 문의 목록 조회",
        "description": "현재 로그인한 사용자가 작성한 1:1 문의 목록을 페이징하여 조회합니다.",
        "operationId": "getMyInquiries",
        "parameters": [
          {
            "name": "pageable",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          },
          {
            "name": "page",
            "in": "query",
            "description": "페이지 번호 (0부터 시작)",
            "example": 0
          },
          {
            "name": "size",
            "in": "query",
            "description": "페이지 당 항목 수",
            "example": 10
          },
          {
            "name": "sort",
            "in": "query",
            "description": "정렬 조건 (예: inquiredAt,desc)"
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageInquirySummaryResponse"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Inquiry (User)"
        ],
        "summary": "1:1 문의 등록",
        "description": "새로운 1:1 문의를 등록합니다.",
        "operationId": "createInquiry",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/InquiryCreateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InquirySummaryResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/cs/inquiry/{inquiryId}/attachments": {
      "post": {
        "tags": [
          "Inquiry (User)"
        ],
        "summary": "1:1 문의 파일 첨부",
        "description": "특정 1:1 문의에 파일을 첨부합니다.",
        "operationId": "addAttachments_1",
        "parameters": [
          {
            "name": "inquiryId",
            "in": "path",
            "description": "파일을 첨부할 문의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "multipart/form-data": {
              "schema": {
                "type": "object",
                "properties": {
                  "files": {
                    "type": "array",
                    "description": "첨부할 파일 목록",
                    "items": {
                      "type": "string",
                      "format": "binary"
                    }
                  }
                },
                "required": [
                  "files"
                ]
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "OK"
          }
        }
      }
    },
    "/api/catalog/artists": {
      "get": {
        "tags": [
          "artist-controller"
        ],
        "operationId": "getArtists",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseListArtistResponse"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "artist-controller"
        ],
        "operationId": "createArtist",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ArtistCreateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseArtistResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/auctions": {
      "get": {
        "tags": [
          "auction-controller"
        ],
        "operationId": "getAuctions",
        "parameters": [
          {
            "name": "saleType",
            "in": "query",
            "required": false,
            "schema": {
              "type": "string",
              "enum": [
                "AUCTION",
                "INSTANT_BUY"
              ]
            }
          },
          {
            "name": "saleStatus",
            "in": "query",
            "required": false,
            "schema": {
              "type": "string",
              "enum": [
                "PREPARING",
                "ACTIVE",
                "SOLD",
                "NOT_SOLD",
                "CANCELLED"
              ]
            }
          },
          {
            "name": "pageable",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageAuctionSummaryResponse"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "auction-controller"
        ],
        "summary": "새로운 판매 상품 등록",
        "description": "새로운 판매 상품 등록",
        "operationId": "createAuction",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/AuctionRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/settlement/settings": {
      "get": {
        "tags": [
          "Admin Settlement Management"
        ],
        "summary": "Get Settlement Settings",
        "description": "현재 정산 설정을 조회합니다.",
        "operationId": "getSettlementSettings",
        "responses": {
          "200": {
            "description": "정산 설정 정보",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/SettlementSettingResponse"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Admin Settlement Management"
        ],
        "summary": "Create or Update Settlement Settings",
        "description": "정산 설정을 생성하거나 수정합니다.",
        "operationId": "createOrUpdateSettlementSettings",
        "requestBody": {
          "description": "정산 설정 요청 데이터",
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/SettlementSettingRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "생성 또는 수정된 정산 설정 정보",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/SettlementSettingResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/returns": {
      "get": {
        "tags": [
          "Refund/Return (Admin)"
        ],
        "summary": "환불/반품 요청 목록 검색 (관리자)",
        "description": "모든 환불/반품 요청 목록을 검색 조건에 따라 페이징하여 조회합니다.",
        "operationId": "searchReturnRequests",
        "parameters": [
          {
            "name": "request",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/ReturnSearchRequest"
            }
          },
          {
            "name": "pageable",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          },
          {
            "name": "status",
            "in": "query",
            "description": "검색할 처리 상태 (REQUESTED, IN_TRANSIT, INSPECTING, APPROVED, REJECTED, COMPLETED, USER_CANCELLED, DELETED)"
          },
          {
            "name": "buyerName",
            "in": "query",
            "description": "검색할 구매자 이름 (부분 일치)"
          },
          {
            "name": "page",
            "in": "query",
            "description": "페이지 번호 (0부터 시작)",
            "example": 0
          },
          {
            "name": "size",
            "in": "query",
            "description": "페이지 당 항목 수",
            "example": 10
          },
          {
            "name": "sort",
            "in": "query",
            "description": "정렬 조건 (예: createdAt,desc)"
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageReturnSummaryResponse"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Refund/Return (Admin)"
        ],
        "summary": "환불/반품 요청 수동 생성 (관리자)",
        "description": "관리자가 사용자를 대신하여 환불/반품 요청을 생성합니다.",
        "operationId": "createReturnRequest_1",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ReturnAdminCreateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ReturnAdminResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/inspections/{productInspectionId}/secondReject": {
      "post": {
        "tags": [
          "admin-inspection-controller"
        ],
        "summary": "2차 검수 반려",
        "description": "2차 검수 반려",
        "operationId": "rejectSecondInspection",
        "parameters": [
          {
            "name": "productInspectionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/OfflineInspectionRejectRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseVoid"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/inspections/{productInspectionId}/secondApprove": {
      "post": {
        "tags": [
          "admin-inspection-controller"
        ],
        "summary": "2차 검수 승인",
        "description": "2차 검수 승인",
        "operationId": "approveSecondInspection",
        "parameters": [
          {
            "name": "productInspectionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/OfflineInspectionApproveRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseVoid"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/inspections/{productInspectionId}/firstReject": {
      "post": {
        "tags": [
          "admin-inspection-controller"
        ],
        "summary": "1차 검수 반려",
        "description": "1차 검수 반려",
        "operationId": "rejectFirstInspection",
        "parameters": [
          {
            "name": "productInspectionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/InspectionRejectRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseVoid"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/inspections/{productInspectionId}/firstApprove": {
      "post": {
        "tags": [
          "admin-inspection-controller"
        ],
        "summary": "1차 검수 승인",
        "description": "1차 검수 승인",
        "operationId": "approveFirstInspection",
        "parameters": [
          {
            "name": "productInspectionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseVoid"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/notices": {
      "get": {
        "tags": [
          "Notice (Admin)"
        ],
        "operationId": "searchNotices",
        "parameters": [
          {
            "name": "request",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/NoticeSearchRequest"
            }
          },
          {
            "name": "pageable",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageNoticeSummaryResponse"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Notice (Admin)"
        ],
        "summary": "새로운 공지사항을 등록합니다.",
        "description": "새로운 공지사항을 등록합니다.",
        "operationId": "createNotice",
        "requestBody": {
          "description": "공지사항 생성에 필요한 데이터 (제목, 내용, 카테고리 ID). \n                    \u003Cp\u003E\u003Cb\u003E[카테고리(csTypeId) ID]\u003C/b\u003E\u003C/p\u003E\n                    \u003Cul\u003E\n                      \u003Cli\u003E1: 배송문의\u003C/li\u003E\n                      \u003Cli\u003E2: 결제문의\u003C/li\u003E\n                      \u003Cli\u003E3: 기타문의\u003C/li\u003E\n                      \u003Cli\u003E4: 상품문의\u003C/li\u003E\n                      \u003Cli\u003E5: 환불/반품 문의\u003C/li\u003E\n                      \u003Cli\u003E6: 판매 문의\u003C/li\u003E\n                    \u003C/ul\u003E",
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/NoticeCreateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "생성된 공지사항의 상세 정보.",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/NoticeDetailResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/notices/{noticeId}/attachments": {
      "post": {
        "tags": [
          "Notice (Admin)"
        ],
        "summary": "특정 공지사항에 파일을 첨부합니다.",
        "description": "특정 공지사항에 파일을 첨부합니다.",
        "operationId": "addAttachments_2",
        "parameters": [
          {
            "name": "noticeId",
            "in": "path",
            "description": "파일을 첨부할 공지사항의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "multipart/form-data": {
              "schema": {
                "type": "object",
                "properties": {
                  "files": {
                    "type": "array",
                    "description": "첨부할 파일 목록",
                    "items": {
                      "type": "string",
                      "format": "binary"
                    }
                  }
                },
                "required": [
                  "files"
                ]
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "작업 성공 시 200 OK."
          }
        }
      }
    },
    "/api/admin/cs/faq": {
      "get": {
        "tags": [
          "FAQ (Admin)"
        ],
        "summary": "FAQ 목록 동적 검색 (관리자)",
        "description": "다양한 조건으로 FAQ 목록을 검색합니다.",
        "operationId": "searchFaqs",
        "parameters": [
          {
            "name": "request",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/FaqSearchRequest"
            }
          },
          {
            "name": "pageable",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          },
          {
            "name": "csTypeId",
            "in": "query",
            "description": "카테고리 ID (1:배송, 2:결제 등)",
            "example": 1
          },
          {
            "name": "keyword",
            "in": "query",
            "description": "검색할 키워드",
            "example": "환불"
          },
          {
            "name": "status",
            "in": "query",
            "description": "FAQ 상태 (DRAFT, ACTIVE, PINNED, INACTIVE)"
          },
          {
            "name": "page",
            "in": "query",
            "description": "페이지 번호 (0부터 시작)",
            "example": 0
          },
          {
            "name": "size",
            "in": "query",
            "description": "페이지 당 항목 수",
            "example": 10
          },
          {
            "name": "sort",
            "in": "query",
            "description": "정렬 조건 (예: faqId,desc)"
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageFaqResponse"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "FAQ (Admin)"
        ],
        "summary": "신규 FAQ 등록",
        "description": "새로운 FAQ를 시스템에 등록합니다.",
        "operationId": "createFaq",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/FaqCreateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "201": {
            "description": "FAQ 생성 성공",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/FaqResponse"
                }
              }
            }
          },
          "400": {
            "description": "잘못된 요청 데이터",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/FaqResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/faq/{faqId}/attachments": {
      "post": {
        "tags": [
          "FAQ (Admin)"
        ],
        "summary": "FAQ 파일 첨부",
        "description": "특정 FAQ에 파일을 첨부합니다.",
        "operationId": "addAttachments_3",
        "parameters": [
          {
            "name": "faqId",
            "in": "path",
            "description": "파일을 첨부할 FAQ의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "multipart/form-data": {
              "schema": {
                "type": "object",
                "properties": {
                  "files": {
                    "type": "array",
                    "description": "첨부할 파일 목록",
                    "items": {
                      "type": "string",
                      "format": "binary"
                    }
                  }
                },
                "required": [
                  "files"
                ]
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "파일 첨부 성공"
          },
          "404": {
            "description": "FAQ를 찾을 수 없음"
          }
        }
      }
    },
    "/api/address/": {
      "get": {
        "tags": [
          "address-controller"
        ],
        "operationId": "getAddresses",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "address-controller"
        ],
        "operationId": "createAddress",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/AddressRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/account/": {
      "get": {
        "tags": [
          "account-controller"
        ],
        "operationId": "getAccount_1",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "account-controller"
        ],
        "operationId": "createAccount",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/AccountRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/returns/{returnRequestId}/cancel": {
      "patch": {
        "tags": [
          "Refund/Return (User)"
        ],
        "summary": "나의 환불/반품 요청 철회",
        "description": "사용자가 직접 자신의 환불/반품 요청을 철회합니다. 처리 시작 전 요청만 철회 가능합니다.",
        "operationId": "cancelReturnRequest",
        "parameters": [
          {
            "name": "returnRequestId",
            "in": "path",
            "description": "철회할 요청 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK"
          }
        }
      }
    },
    "/api/report/member/received": {
      "patch": {
        "tags": [
          "report-controller"
        ],
        "operationId": "setReceivedReport",
        "parameters": [
          {
            "name": "reportId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/auctions/{auctionId}/status/sold": {
      "patch": {
        "tags": [
          "auction-controller"
        ],
        "summary": "상품 판매 완료 처리",
        "description": "상품 판매 완료 처리",
        "operationId": "markAsSold",
        "parameters": [
          {
            "name": "auctionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "finalPrice",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/auctions/{auctionId}/status/not-sold": {
      "patch": {
        "tags": [
          "auction-controller"
        ],
        "summary": "상품을 판매 실패(NOT_SOLD) 상태로 변경",
        "description": "상품을 판매 실패(NOT_SOLD) 상태로 변경",
        "operationId": "markAsNotSold",
        "parameters": [
          {
            "name": "auctionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/auctions/{auctionId}/status/cancelled": {
      "patch": {
        "tags": [
          "auction-controller"
        ],
        "summary": "상품을 취소(CANCELLED) 상태로 변경",
        "description": "상품을 취소(CANCELLED) 상태로 변경",
        "operationId": "cancelAuction",
        "parameters": [
          {
            "name": "auctionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/auctions/{auctionId}/status/active": {
      "patch": {
        "tags": [
          "auction-controller"
        ],
        "summary": "상품을 활성(ACTIVE) 상태로 변경",
        "description": "상품을 활성(ACTIVE) 상태로 변경",
        "operationId": "activateAuction",
        "parameters": [
          {
            "name": "auctionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/auctions/{auctionId}/sale-type": {
      "patch": {
        "tags": [
          "auction-controller"
        ],
        "summary": "상품 판매 유형 변경",
        "description": "상품 판매 유형 변경",
        "operationId": "changeSaleType",
        "parameters": [
          {
            "name": "auctionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "newSaleType",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/returns/{returnRequestId}": {
      "get": {
        "tags": [
          "Refund/Return (Admin)"
        ],
        "summary": "환불/반품 요청 상세 조회 (관리자)",
        "description": "특정 환불/반품 요청의 상세 정보를 조회합니다.",
        "operationId": "getReturnRequestDetail",
        "parameters": [
          {
            "name": "returnRequestId",
            "in": "path",
            "description": "조회할 요청 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ReturnAdminResponse"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Refund/Return (Admin)"
        ],
        "summary": "환불/반품 요청 삭제 (관리자)",
        "description": "관리자가 환불/반품 요청을 논리적으로 삭제합니다.",
        "operationId": "deleteReturnRequest",
        "parameters": [
          {
            "name": "returnRequestId",
            "in": "path",
            "description": "삭제할 요청 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK"
          }
        }
      },
      "patch": {
        "tags": [
          "Refund/Return (Admin)"
        ],
        "summary": "환불/반품 요청 상태 변경 (관리자)",
        "description": "특정 환불/반품 요청의 상태를 변경하고 관련 정보를 업데이트합니다.",
        "operationId": "updateReturnRequestStatus",
        "parameters": [
          {
            "name": "returnRequestId",
            "in": "path",
            "description": "처리할 요청 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ReturnAdminUpdateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ReturnAdminResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/notices/{noticeId}": {
      "get": {
        "tags": [
          "Notice (Admin)"
        ],
        "summary": "특정 공지사항의 상세 정보를 조회합니다",
        "description": "특정 공지사항의 상세 정보를 조회합니다.\n \u003Cp\u003E이 API는 사용자용 API와 동일한 기능을 수행합니다.",
        "operationId": "getNoticeDetail",
        "parameters": [
          {
            "name": "noticeId",
            "in": "path",
            "description": "조회할 공지사항의 ID.",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "공지사항 상세 정보.",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/NoticeDetailResponse"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Notice (Admin)"
        ],
        "summary": "특정 공지사항을 삭제합니다.",
        "description": "특정 공지사항을 삭제합니다.",
        "operationId": "deleteNotice",
        "parameters": [
          {
            "name": "noticeId",
            "in": "path",
            "description": "삭제할 공지사항의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "작업 성공 시 204 No Content."
          }
        }
      },
      "patch": {
        "tags": [
          "Notice (Admin)"
        ],
        "summary": "특정 공지사항의 내용을 수정합니다.",
        "description": "특정 공지사항의 내용을 수정합니다.",
        "operationId": "updateNotice",
        "parameters": [
          {
            "name": "noticeId",
            "in": "path",
            "description": "수정할 공지사항의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "description": "공지사항 수정에 필요한 데이터.\n                    \u003Cp\u003E\u003Cb\u003E[변경 가능한 상태(status)]\u003C/b\u003E\u003C/p\u003E\n                    \u003Cul\u003E\n                       \u003Cli\u003EDRAFT: 임시저장\u003C/li\u003E\n                       \u003Cli\u003EACTIVE: 활성 (노출)\u003C/li\u003E\n                       \u003Cli\u003EPINNED: 상단 고정\u003C/li\u003E\n                       \u003Cli\u003EINACTIVE: 비활성 (미노출)\u003C/li\u003E\n                    \u003C/ul\u003E\n                    \u003Cp\u003E\u003Cb\u003E[카테고리(csTypeId) ID]\u003C/b\u003E\u003C/p\u003E\n                    \u003Cul\u003E\n                      \u003Cli\u003E1: 배송문의\u003C/li\u003E\n                      \u003Cli\u003E2: 결제문의\u003C/li\u003E\n                      \u003Cli\u003E3: 기타문의\u003C/li\u003E\n                      \u003Cli\u003E4: 상품문의\u003C/li\u003E\n                      \u003Cli\u003E5: 환불/반품 문의\u003C/li\u003E\n                      \u003Cli\u003E6: 판매 문의\u003C/li\u003E\n                    \u003C/ul\u003E",
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/NoticeUpdateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "수정된 공지사항의 상세 정보.",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/NoticeDetailResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/inquiry/{inquiryId}/answer": {
      "patch": {
        "tags": [
          "Inquiry (Admin)"
        ],
        "summary": "1:1 문의 답변 및 상태 변경",
        "description": "특정 1:1 문의에 답변을 등록하고 처리 상태를 변경합니다.",
        "operationId": "answerInquiry",
        "parameters": [
          {
            "name": "inquiryId",
            "in": "path",
            "description": "답변할 문의의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/InquiryAnswerRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InquiryDetailAdminResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/faq/{faqId}": {
      "get": {
        "tags": [
          "FAQ (Admin)"
        ],
        "summary": "FAQ 상세 정보 조회",
        "description": "특정 FAQ의 상세 정보를 조회합니다.",
        "operationId": "getFaqDetail",
        "parameters": [
          {
            "name": "faqId",
            "in": "path",
            "description": "조회할 FAQ의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "조회 성공",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/FaqResponse"
                }
              }
            }
          },
          "404": {
            "description": "FAQ를 찾을 수 없음",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/FaqResponse"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "FAQ (Admin)"
        ],
        "summary": "FAQ 삭제",
        "description": "특정 FAQ를 시스템에서 삭제합니다.",
        "operationId": "deleteFaq",
        "parameters": [
          {
            "name": "faqId",
            "in": "path",
            "description": "삭제할 FAQ의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "204": {
            "description": "FAQ 삭제 성공"
          },
          "404": {
            "description": "FAQ를 찾을 수 없음"
          }
        }
      },
      "patch": {
        "tags": [
          "FAQ (Admin)"
        ],
        "summary": "FAQ 내용 수정",
        "description": "특정 FAQ의 제목, 내용, 상태 등을 수정합니다.",
        "operationId": "updateFaq",
        "parameters": [
          {
            "name": "faqId",
            "in": "path",
            "description": "수정할 FAQ의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/FaqUpdateRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "FAQ 수정 성공",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/FaqResponse"
                }
              }
            }
          },
          "400": {
            "description": "잘못된 요청 데이터",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/FaqResponse"
                }
              }
            }
          },
          "404": {
            "description": "FAQ를 찾을 수 없음",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/FaqResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/address/default": {
      "patch": {
        "tags": [
          "address-controller"
        ],
        "operationId": "setDefaultAddress",
        "parameters": [
          {
            "name": "memberId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "addressId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/user/findId": {
      "get": {
        "tags": [
          "signup-controller"
        ],
        "operationId": "findId",
        "parameters": [
          {
            "name": "email",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/user/checkId": {
      "get": {
        "tags": [
          "signup-controller"
        ],
        "operationId": "checkIdDuplicate",
        "parameters": [
          {
            "name": "id",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/returns/{returnRequestId}": {
      "get": {
        "tags": [
          "Refund/Return (User)"
        ],
        "summary": "나의 환불/반품 요청 상세 조회",
        "description": "나의 특정 환불/반품 요청 상세 정보를 조회합니다.",
        "operationId": "getMyReturnRequestDetail",
        "parameters": [
          {
            "name": "returnRequestId",
            "in": "path",
            "description": "조회할 요청 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ReturnDetailResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/report/received": {
      "get": {
        "tags": [
          "report-controller"
        ],
        "operationId": "getReceivedReports",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/report/member/{memberId}": {
      "get": {
        "tags": [
          "report-controller"
        ],
        "operationId": "getReportMember",
        "parameters": [
          {
            "name": "memberId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/pricing/marketAverage": {
      "get": {
        "tags": [
          "pricing-controller"
        ],
        "operationId": "getMarketAveragePrice",
        "parameters": [
          {
            "name": "goodsCategoryId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32",
              "exclusiveMinimum": 0
            }
          },
          {
            "name": "artistId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32",
              "exclusiveMinimum": 0
            }
          },
          {
            "name": "albumId",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseMarketAvgPriceResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/pricing/baseline": {
      "get": {
        "tags": [
          "pricing-controller"
        ],
        "operationId": "getPriceBaselineByCategoryId",
        "parameters": [
          {
            "name": "goodsCategoryId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32",
              "exclusiveMinimum": 0
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseDouble"
                }
              }
            }
          }
        }
      }
    },
    "/api/payments/{orderId}/verify": {
      "get": {
        "tags": [
          "payment-controller"
        ],
        "operationId": "requestPaymentVerify",
        "parameters": [
          {
            "name": "orderId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ApiResponsePayment"
                }
              }
            }
          }
        }
      }
    },
    "/api/my/settlements": {
      "get": {
        "tags": [
          "My Settlement"
        ],
        "summary": "Get My Settlement List",
        "description": "나의 정산 내역 목록을 페이징하여 조회합니다.",
        "operationId": "getMySettlements",
        "parameters": [
          {
            "name": "pageable",
            "in": "query",
            "description": "페이징 정보",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          },
          {
            "name": "page",
            "in": "query",
            "description": "페이지 번호 (0부터 시작)",
            "example": 0
          },
          {
            "name": "size",
            "in": "query",
            "description": "페이지 당 항목 수",
            "example": 10
          },
          {
            "name": "sort",
            "in": "query",
            "description": "정렬 조건 (예: requestedAt,desc)"
          }
        ],
        "responses": {
          "200": {
            "description": "페이징된 정산 내역 목록",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageSettlementSummaryResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/my/settlements/{settlementId}": {
      "get": {
        "tags": [
          "My Settlement"
        ],
        "summary": "Get My Settlement Detail",
        "description": "나의 특정 정산 건의 상세 내역을 조회합니다.",
        "operationId": "getMySettlementDetail",
        "parameters": [
          {
            "name": "settlementId",
            "in": "path",
            "description": "조회할 정산 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "정산 상세 정보",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/SettlementDetailResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/member/{id}/verifyPassword": {
      "get": {
        "tags": [
          "member-controller"
        ],
        "operationId": "verifyPassword",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "password",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/member/me": {
      "get": {
        "tags": [
          "member-controller"
        ],
        "operationId": "getMember",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/inspections/my": {
      "get": {
        "tags": [
          "user-inspection-controller"
        ],
        "summary": "나의 검수 현황 목록 조회",
        "description": "나의 검수 현황 목록 조회",
        "operationId": "getMyInspections",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseListMyInspectionResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/file/{fileMetaId}": {
      "get": {
        "tags": [
          "test-controller"
        ],
        "operationId": "getFileAccessUrl",
        "parameters": [
          {
            "name": "fileMetaId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "test-controller"
        ],
        "operationId": "deleteFile",
        "parameters": [
          {
            "name": "fileMetaId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/cs/notices": {
      "get": {
        "tags": [
          "notice-controller"
        ],
        "summary": "(사용자용) 공지사항 목록을 검색 조건에 따라 페이징하여 조회합니다.",
        "description": "(사용자용) 공지사항 목록을 검색 조건에 따라 페이징하여 조회합니다.\n 공개된(ACTIVE, PINNED) 상태의 공지사항만 조회됩니다.",
        "operationId": "searchNotices_1",
        "parameters": [
          {
            "name": "request",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/NoticeSearchRequest"
            }
          },
          {
            "name": "pageable",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageNoticeSummaryResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/cs/notices/{noticeId}": {
      "get": {
        "tags": [
          "notice-controller"
        ],
        "summary": "특정 공지사항의 상세 정보를 조회합니다.",
        "description": "특정 공지사항의 상세 정보를 조회합니다.",
        "operationId": "getNoticeDetail_1",
        "parameters": [
          {
            "name": "noticeId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/NoticeDetailResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/cs/inquiry/{inquiryId}": {
      "get": {
        "tags": [
          "Inquiry (User)"
        ],
        "summary": "나의 1:1 문의 상세 조회",
        "description": "나의 특정 1:1 문의 상세 내용을 조회합니다.",
        "operationId": "getMyInquiryDetail",
        "parameters": [
          {
            "name": "inquiryId",
            "in": "path",
            "description": "조회할 문의의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InquiryDetailUserResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/cs/faq": {
      "get": {
        "tags": [
          "FAQ (User)"
        ],
        "summary": "FAQ 목록 동적 검색 (사용자)",
        "description": "공개된 FAQ 목록을 검색합니다.",
        "operationId": "searchFaqs_1",
        "parameters": [
          {
            "name": "request",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/FaqSearchRequest"
            }
          },
          {
            "name": "pageable",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          },
          {
            "name": "csTypeId",
            "in": "query",
            "description": "카테고리 ID (1:배송, 2:결제 등)",
            "example": 1
          },
          {
            "name": "keyword",
            "in": "query",
            "description": "검색할 키워드",
            "example": "배송"
          },
          {
            "name": "page",
            "in": "query",
            "description": "페이지 번호 (0부터 시작)",
            "example": 0
          },
          {
            "name": "size",
            "in": "query",
            "description": "페이지 당 항목 수",
            "example": 10
          },
          {
            "name": "sort",
            "in": "query",
            "description": "정렬 조건 (예: faqId,desc)"
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageFaqResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/checklists": {
      "get": {
        "tags": [
          "checklist-controller"
        ],
        "operationId": "getChecklistsByCategory",
        "parameters": [
          {
            "name": "goodsCategoryId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32",
              "exclusiveMinimum": 0
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseOnlineChecklistResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/catalog/goodsCategories": {
      "get": {
        "tags": [
          "goods-category-controller"
        ],
        "operationId": "getGoodsCategories",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseListGoodsCategoryResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/catalog/albums": {
      "get": {
        "tags": [
          "album-controller"
        ],
        "operationId": "getAlbumsByArtist",
        "parameters": [
          {
            "name": "artistId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32",
              "exclusiveMinimum": 0
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseListAlbumResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/bids/auctions/{auctionId}": {
      "get": {
        "tags": [
          "bid-api-controller"
        ],
        "operationId": "getBidsByAuctionId",
        "parameters": [
          {
            "name": "auctionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/Bid"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/api/auctions/{auctionId}": {
      "get": {
        "tags": [
          "auction-controller"
        ],
        "summary": "상품 단건 조회",
        "description": "상품 단건 조회",
        "operationId": "getAuctionById",
        "parameters": [
          {
            "name": "auctionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "auction-controller"
        ],
        "summary": "판매 상품 삭제",
        "description": "판매 상품 삭제",
        "operationId": "deleteAuction",
        "parameters": [
          {
            "name": "auctionId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "string"
                }
              }
            }
          }
        }
      }
    },
    "/api/auctions/member/{memberId}": {
      "get": {
        "tags": [
          "auction-controller"
        ],
        "summary": "특정 회원의 모든 판매 상품 조회",
        "description": "특정 회원의 모든 판매 상품 조회",
        "operationId": "getAuctionsByMember",
        "parameters": [
          {
            "name": "memberId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/Auction"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/api/auctions/member/{memberId}/status": {
      "get": {
        "tags": [
          "auction-controller"
        ],
        "summary": "특정 회원의 판매 상태별 상품 조회",
        "description": "특정 회원의 판매 상태별 상품 조회",
        "operationId": "getAuctionsByMemberAndStatus",
        "parameters": [
          {
            "name": "memberId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "saleStatus",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string",
              "enum": [
                "PREPARING",
                "ACTIVE",
                "SOLD",
                "NOT_SOLD",
                "CANCELLED"
              ]
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/Auction"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/settlement": {
      "get": {
        "tags": [
          "Admin Settlement Management"
        ],
        "summary": "Get Settlement List",
        "description": "정산 내역 목록을 검색하고 페이징하여 조회합니다.",
        "operationId": "getSettlements",
        "parameters": [
          {
            "name": "condition",
            "in": "query",
            "description": "검색 조건 (판매자명, 상태, 기간 등)",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/SettlementSearchCondition"
            }
          },
          {
            "name": "pageable",
            "in": "query",
            "description": "페이징 정보",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          },
          {
            "name": "sellerName",
            "in": "query",
            "description": "검색할 판매자 이름 (부분 일치)"
          },
          {
            "name": "status",
            "in": "query",
            "description": "검색할 정산 상태 (PENDING, PAID, CANCELLED, FAILED)"
          },
          {
            "name": "startDate",
            "in": "query",
            "description": "검색 시작일 (YYYY-MM-DD)",
            "example": "2025-10-01"
          },
          {
            "name": "endDate",
            "in": "query",
            "description": "검색 종료일 (YYYY-MM-DD)",
            "example": "2025-10-31"
          },
          {
            "name": "page",
            "in": "query",
            "description": "페이지 번호 (0부터 시작)",
            "example": 0
          },
          {
            "name": "size",
            "in": "query",
            "description": "페이지 당 항목 수",
            "example": 10
          },
          {
            "name": "sort",
            "in": "query",
            "description": "정렬 조건 (예: requestedAt,desc)"
          }
        ],
        "responses": {
          "200": {
            "description": "페이징된 정산 내역 목록",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageSettlementSummaryResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/settlement/{settlementId}": {
      "get": {
        "tags": [
          "Admin Settlement Management"
        ],
        "summary": "Get Settlement Detail",
        "description": "특정 정산 건의 상세 내역을 조회합니다.",
        "operationId": "getSettlementDetail",
        "parameters": [
          {
            "name": "settlementId",
            "in": "path",
            "description": "조회할 정산 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "정산 상세 정보",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/SettlementDetailResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/settlement/stats": {
      "get": {
        "tags": [
          "Admin Settlement Management"
        ],
        "summary": "정산 상태별 통계 조회",
        "description": "정산의 상태(대기, 완료, 취소, 실패)별 개수를 조회합니다.",
        "operationId": "getSettlementStats",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/SettlementStatsResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/settlement/dashboard": {
      "get": {
        "tags": [
          "Admin Settlement Management"
        ],
        "summary": "Get Settlement Dashboard Summary",
        "description": "대시보드용 정산 KPI 데이터를 조회합니다.",
        "operationId": "getDashboardSummary",
        "responses": {
          "200": {
            "description": "정산 KPI 데이터",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/SettlementDashboardResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/returns/stats": {
      "get": {
        "tags": [
          "Refund/Return (Admin)"
        ],
        "summary": "환불/반품 상태별 통계 조회",
        "description": "환불/반품 요청의 상태별 개수를 조회합니다. (삭제된 건 제외)",
        "operationId": "getReturnStats",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/ReturnStatsResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/inspections": {
      "get": {
        "tags": [
          "admin-inspection-controller"
        ],
        "summary": "상태별 신청 목록 (페이지)",
        "description": "상태별 신청 목록 (페이지)",
        "operationId": "listByStatus",
        "parameters": [
          {
            "name": "statuses",
            "in": "query",
            "description": "조회할 검수 상태 목록 (e.g. ?statuses=SUBMITTED,FIRST_REVIEWED)",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string",
                "enum": [
                  "DRAFT",
                  "SUBMITTED",
                  "ONLINE_APPROVED",
                  "ONLINE_REJECTED",
                  "OFFLINE_INSPECTING",
                  "OFFLINE_REJECTED",
                  "COMPLETED"
                ]
              }
            }
          },
          {
            "name": "pageable",
            "in": "query",
            "description": "페이지네이션, 정렬 정보 (e.g. ?page=0&size=20&sort=submittedAt,desc)",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "페이징 처리된 검수 목록",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseInspectionPageResponseInspectionListResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/inspections/online/{productInspectionId}": {
      "get": {
        "tags": [
          "admin-inspection-controller"
        ],
        "summary": "관리자 1차 온라인 검수 상세 조회",
        "description": "관리자 1차 온라인 검수 상세 조회",
        "operationId": "getOnlineInspectionDetail",
        "parameters": [
          {
            "name": "productInspectionId",
            "in": "path",
            "description": "조회할 검수 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "1차 온라인 검수 상세 정보",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseOnlineInspectionDetailResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/inspections/offline/{productInspectionId}": {
      "get": {
        "tags": [
          "admin-inspection-controller"
        ],
        "summary": "관리자 2차 오프라인 검수 상세 조회",
        "description": "관리자 2차 오프라인 검수 상세 조회",
        "operationId": "getOfflineInspectionDetail",
        "parameters": [
          {
            "name": "productInspectionId",
            "in": "path",
            "description": "조회할 검수 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "2차 오프라인 상세 정보",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InspectionApiResponseOfflineInspectionDetailResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/dashboard/summary": {
      "get": {
        "tags": [
          "Admin Dashboard Management"
        ],
        "summary": "Get Dashboard Summary",
        "description": "대시보드 요약 정보를 조회합니다.",
        "operationId": "getDashboardSummary_1",
        "responses": {
          "200": {
            "description": "DashboardSummaryResponse DTO",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/DashboardSummaryResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/notices/stats": {
      "get": {
        "tags": [
          "Notice (Admin)"
        ],
        "summary": "공지사항 상태별 통계 조회",
        "description": "공지사항의 상태(초안, 활성, 고정, 비활성)별 개수를 조회합니다.",
        "operationId": "getNoticeStats",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/NoticeStatsResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/inquiry": {
      "get": {
        "tags": [
          "Inquiry (Admin)"
        ],
        "summary": "1:1 문의 목록 동적 검색 (관리자)",
        "description": "모든 1:1 문의 목록을 검색 조건에 따라 페이징하여 조회합니다.",
        "operationId": "searchInquiries",
        "parameters": [
          {
            "name": "condition",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/InquirySearchCondition"
            }
          },
          {
            "name": "pageable",
            "in": "query",
            "required": true,
            "schema": {
              "$ref": "#/components/schemas/Pageable"
            }
          },
          {
            "name": "status",
            "in": "query",
            "description": "검색할 처리 상태 (PENDING, IN_PROGRESS, ON_HOLD, REJECTED, ANSWERED)"
          },
          {
            "name": "csTypeId",
            "in": "query",
            "description": "검색할 문의 유형 ID (1:배송, 2:결제 등)"
          },
          {
            "name": "memberName",
            "in": "query",
            "description": "검색할 작성자 이름 (부분 일치)"
          },
          {
            "name": "page",
            "in": "query",
            "description": "페이지 번호 (0부터 시작)",
            "example": 0
          },
          {
            "name": "size",
            "in": "query",
            "description": "페이지 당 항목 수",
            "example": 10
          },
          {
            "name": "sort",
            "in": "query",
            "description": "정렬 조건 (예: inquiredAt,desc)"
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PageInquirySummaryResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/inquiry/{inquiryId}": {
      "get": {
        "tags": [
          "Inquiry (Admin)"
        ],
        "summary": "1:1 문의 상세 조회 (관리자)",
        "description": "특정 1:1 문의의 상세 내용을 관리자용으로 조회합니다.",
        "operationId": "getInquiryDetail",
        "parameters": [
          {
            "name": "inquiryId",
            "in": "path",
            "description": "조회할 문의의 ID",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InquiryDetailAdminResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/inquiry/stats": {
      "get": {
        "tags": [
          "Inquiry (Admin)"
        ],
        "summary": "1:1 문의 통계 조회 (관리자 대시보드용)",
        "description": "각 문의 처리 상태별 문의 개수를 조회합니다.",
        "operationId": "getInquiryStats",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/InquiryStatsAdminResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/admin/cs/faq/stats": {
      "get": {
        "tags": [
          "FAQ (Admin)"
        ],
        "summary": "FAQ 상태별 통계 조회",
        "description": "FAQ의 상태(초안, 활성, 고정, 비활성)별 개수를 조회합니다.",
        "operationId": "getFaqStats",
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/FaqStatsResponse"
                }
              }
            }
          }
        }
      }
    },
    "/api/address/member/{memberId}": {
      "get": {
        "tags": [
          "address-controller"
        ],
        "operationId": "getAddressMember",
        "parameters": [
          {
            "name": "memberId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/account/member/{memberId}": {
      "get": {
        "tags": [
          "account-controller"
        ],
        "operationId": "getAccountsMember",
        "parameters": [
          {
            "name": "memberId",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "Role": {
        "type": "object",
        "properties": {
          "roleId": {
            "type": "integer",
            "format": "int32"
          },
          "roleType": {
            "type": "string",
            "enum": [
              "SADMIN",
              "ADMIN",
              "USER",
              "RUSER"
            ]
          }
        }
      },
      "ReportRequest": {
        "type": "object",
        "properties": {
          "reportReason": {
            "type": "string"
          },
          "reportStatus": {
            "type": "string",
            "enum": [
              "RESOLVED",
              "RECEIVED",
              "WITHDRAWN",
              "REJECTED"
            ]
          },
          "rejectedComment": {
            "type": "string"
          },
          "memberId": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "MemberUpdateRequest": {
        "type": "object",
        "properties": {
          "id": {
            "type": "string"
          },
          "name": {
            "type": "string"
          },
          "tel": {
            "type": "string"
          },
          "email": {
            "type": "string"
          },
          "isActive": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "AddressRequest": {
        "type": "object",
        "properties": {
          "roadAddress": {
            "type": "string"
          },
          "detailAddress": {
            "type": "string"
          },
          "alias": {
            "type": "string"
          },
          "recipientName": {
            "type": "string"
          },
          "recipientTel": {
            "type": "string"
          },
          "isDefault": {
            "type": "string"
          },
          "memberId": {
            "type": "integer",
            "format": "int32"
          },
          "destinationAddress": {
            "type": "string"
          }
        }
      },
      "AccountRequest": {
        "type": "object",
        "properties": {
          "accountName": {
            "type": "string"
          },
          "bankName": {
            "type": "string"
          },
          "isActive": {
            "type": "string"
          },
          "isRefundable": {
            "type": "string"
          },
          "memberId": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "MemberDTO": {
        "type": "object",
        "properties": {
          "username": {
            "type": "string",
            "minLength": 1,
            "pattern": "^[a-zA-Z0-9]{6,20}$"
          },
          "password": {
            "type": "string",
            "pattern": "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$"
          },
          "name": {
            "type": "string"
          },
          "email": {
            "type": "string",
            "format": "email",
            "pattern": "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
          },
          "phone": {
            "type": "string",
            "minLength": 1,
            "pattern": "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$"
          },
          "mailCode": {
            "type": "string"
          }
        },
        "required": [
          "phone",
          "username"
        ]
      },
      "AuthCodeDTO": {
        "type": "object",
        "properties": {
          "email": {
            "type": "string"
          },
          "code": {
            "type": "string"
          }
        }
      },
      "MailRequest": {
        "type": "object",
        "properties": {
          "to": {
            "type": "string"
          },
          "subject": {
            "type": "string"
          },
          "text": {
            "type": "string"
          },
          "link": {
            "type": "string"
          },
          "linkTitle": {
            "type": "string"
          }
        }
      },
      "ReturnCreateRequest": {
        "type": "object",
        "properties": {
          "orderId": {
            "type": "string",
            "minLength": 1
          },
          "reason": {
            "type": "string",
            "enum": [
              "SIMPLE_CHANGE_OF_MIND",
              "PRODUCT_DEFECT",
              "SHIPPING_ERROR",
              "ETC"
            ]
          },
          "detailReason": {
            "type": "string",
            "maxLength": 2000,
            "minLength": 0
          }
        },
        "required": [
          "orderId",
          "reason"
        ]
      },
      "ReturnDetailResponse": {
        "type": "object",
        "description": "사용자에게 보여줄 환불/반품 요청의 상세 정보를 담는 DTO입니다.\n \u003Cp\u003E관리자용 응답과 달리, 처리자 정보나 내부 메모 등 민감한 정보는 제외됩니다.",
        "properties": {
          "returnRequestId": {
            "type": "integer",
            "format": "int32"
          },
          "orderId": {
            "type": "integer",
            "format": "int32"
          },
          "reason": {
            "type": "string",
            "enum": [
              "SIMPLE_CHANGE_OF_MIND",
              "PRODUCT_DEFECT",
              "SHIPPING_ERROR",
              "ETC"
            ]
          },
          "detailReason": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "REQUESTED",
              "IN_TRANSIT",
              "INSPECTING",
              "APPROVED",
              "REJECTED",
              "COMPLETED",
              "USER_CANCELLED",
              "DELETED"
            ]
          },
          "finalRefundAmount": {
            "type": "number"
          },
          "requestedAt": {
            "type": "string",
            "format": "date-time"
          },
          "completedAt": {
            "type": "string",
            "format": "date-time"
          },
          "attachmentUrls": {
            "type": "array",
            "items": {
              "type": "string"
            }
          },
          "statusHistories": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ReturnHistoryResponse"
            }
          }
        }
      },
      "ReturnHistoryResponse": {
        "type": "object",
        "description": "환불/반품 요청의 상태 변경 이력 한 건을 나타내는 응답 DTO입니다.\n \u003Cp\u003E상세 응답 DTO에 포함되어, 언제, 누구에 의해, 어떤 상태로 변경되었는지 보여주는 데 사용됩니다.",
        "properties": {
          "previousStatus": {
            "type": "string",
            "enum": [
              "REQUESTED",
              "IN_TRANSIT",
              "INSPECTING",
              "APPROVED",
              "REJECTED",
              "COMPLETED",
              "USER_CANCELLED",
              "DELETED"
            ]
          },
          "newStatus": {
            "type": "string",
            "enum": [
              "REQUESTED",
              "IN_TRANSIT",
              "INSPECTING",
              "APPROVED",
              "REJECTED",
              "COMPLETED",
              "USER_CANCELLED",
              "DELETED"
            ]
          },
          "updatedBy": {
            "type": "string"
          },
          "updatedAt": {
            "type": "string",
            "format": "date-time"
          },
          "memo": {
            "type": "string"
          }
        }
      },
      "InspectionApiResponseDouble": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "type": "number",
            "format": "double"
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "PaymentCreateRequest": {
        "type": "object",
        "properties": {
          "memberId": {
            "type": "string",
            "minLength": 1
          },
          "itemId": {
            "type": "string",
            "minLength": 1,
            "pattern": "^\\d{1,10}$"
          },
          "price": {
            "type": "string",
            "minLength": 1,
            "pattern": "^\\d{1,10}$"
          }
        },
        "required": [
          "itemId",
          "memberId",
          "price"
        ]
      },
      "ApiResponsePaymentResponse": {
        "type": "object",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "data": {
            "$ref": "#/components/schemas/PaymentResponse"
          },
          "errorMessage": {
            "type": "string"
          }
        }
      },
      "PaymentResponse": {
        "type": "object",
        "properties": {
          "orderId": {
            "type": "string"
          }
        }
      },
      "BankDataDto": {
        "type": "object",
        "properties": {
          "tid": {
            "type": "string"
          },
          "bankCode": {
            "type": "string"
          },
          "bankName": {
            "type": "string"
          },
          "bankAccount": {
            "type": "string"
          },
          "bankUsername": {
            "type": "string"
          },
          "cashReceiptTid": {
            "type": "string"
          },
          "cashReceiptType": {
            "type": "string"
          },
          "cashReceiptNo": {
            "type": "string"
          },
          "receiptUrl": {
            "type": "string"
          },
          "cashReceiptUrl": {
            "type": "string"
          }
        }
      },
      "PaymentCancelRequest": {
        "type": "object",
        "properties": {
          "receiptId": {
            "type": "string",
            "minLength": 1
          },
          "cancelPrice": {
            "type": "string",
            "minLength": 1,
            "pattern": "^\\d{1,10}$"
          },
          "memberId": {
            "type": "string",
            "minLength": 1
          },
          "cancelReason": {
            "type": "string"
          },
          "bankDataDto": {
            "$ref": "#/components/schemas/BankDataDto"
          }
        },
        "required": [
          "cancelPrice",
          "memberId",
          "receiptId"
        ]
      },
      "ApiResponsePaymentCancelResponse": {
        "type": "object",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "data": {
            "$ref": "#/components/schemas/PaymentCancelResponse"
          },
          "errorMessage": {
            "type": "string"
          }
        }
      },
      "PaymentCancelResponse": {
        "type": "object",
        "properties": {
          "receiptId": {
            "type": "string"
          },
          "orderId": {
            "type": "string"
          },
          "cancelledPrice": {
            "type": "integer",
            "format": "int32"
          },
          "reason": {
            "type": "string"
          },
          "status": {
            "type": "string"
          },
          "cancelledAt": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "PaymentApproveRequest": {
        "type": "object",
        "properties": {
          "receiptData": {
            "type": "string"
          }
        }
      },
      "ApiResponseString": {
        "type": "object",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "data": {
            "type": "string"
          },
          "errorMessage": {
            "type": "string"
          }
        }
      },
      "MemberCreateRequest": {
        "type": "object",
        "properties": {
          "id": {
            "type": "string"
          },
          "password": {
            "type": "string"
          },
          "name": {
            "type": "string"
          },
          "email": {
            "type": "string"
          },
          "tel": {
            "type": "string"
          },
          "sns": {
            "type": "string"
          },
          "roleId": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "InspectionRequest": {
        "type": "object",
        "description": "FE에서 1차 온라인 검수 신청 시 보내는 JSON 데이터를 받기 위한 DTO",
        "properties": {
          "templateId": {
            "type": "integer",
            "format": "int32"
          },
          "templateVersion": {
            "type": "integer",
            "format": "int32"
          },
          "goodsCategoryId": {
            "type": "integer",
            "format": "int32"
          },
          "artistId": {
            "type": "integer",
            "format": "int32"
          },
          "albumId": {
            "type": "integer",
            "format": "int32"
          },
          "itemName": {
            "type": "string"
          },
          "itemDescription": {
            "type": "string"
          },
          "hashtags": {
            "type": "string"
          },
          "answers": {
            "type": "object",
            "additionalProperties": {
              "type": "string"
            }
          },
          "expectedPrice": {
            "type": "number"
          },
          "marketAvgPrice": {
            "type": "number"
          },
          "sellerHopePrice": {
            "type": "number"
          },
          "shippingAddress": {
            "type": "string"
          },
          "shippingAddressDetail": {
            "type": "string"
          },
          "bankName": {
            "type": "string"
          },
          "bankAccount": {
            "type": "string"
          }
        }
      },
      "InspectionApiResponseInteger": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "type": "integer",
            "format": "int32"
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "InspectionApiResponseVoid": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {

          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "FileMeta": {
        "type": "object",
        "properties": {
          "filemetaId": {
            "type": "integer",
            "format": "int32"
          },
          "originalFileName": {
            "type": "string"
          },
          "storedFileName": {
            "type": "string"
          },
          "storedFilePath": {
            "type": "string"
          },
          "fileType": {
            "type": "string",
            "enum": [
              "IMAGE",
              "VIDEO",
              "AUDIO",
              "DOCUMENT",
              "ETC"
            ]
          },
          "fileSize": {
            "type": "integer",
            "format": "int32"
          },
          "fileExt": {
            "type": "string"
          },
          "uploadedAt": {
            "type": "string",
            "format": "date-time"
          },
          "uploadedBy": {
            "$ref": "#/components/schemas/Member"
          },
          "deletedAt": {
            "type": "string",
            "format": "date-time"
          },
          "width": {
            "type": "integer",
            "format": "int32"
          },
          "height": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "Member": {
        "type": "object",
        "properties": {
          "memberId": {
            "type": "integer",
            "format": "int32"
          },
          "id": {
            "type": "string"
          },
          "password": {
            "type": "string"
          },
          "name": {
            "type": "string"
          },
          "email": {
            "type": "string"
          },
          "tel": {
            "type": "string"
          },
          "sns": {
            "type": "string"
          },
          "createAt": {
            "type": "string"
          },
          "leavedAt": {
            "type": "string"
          },
          "isActive": {
            "type": "integer",
            "format": "int32"
          },
          "role": {
            "$ref": "#/components/schemas/Role"
          }
        }
      },
      "InquiryCreateRequest": {
        "type": "object",
        "properties": {
          "csTypeId": {
            "type": "integer",
            "format": "int32"
          },
          "title": {
            "type": "string",
            "maxLength": 100,
            "minLength": 0
          },
          "content": {
            "type": "string",
            "maxLength": 2000,
            "minLength": 0
          }
        },
        "required": [
          "content",
          "csTypeId",
          "title"
        ]
      },
      "InquirySummaryResponse": {
        "type": "object",
        "properties": {
          "inquiryId": {
            "type": "integer",
            "format": "int32"
          },
          "title": {
            "type": "string"
          },
          "content": {
            "type": "string"
          },
          "inquiredByName": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "PENDING",
              "IN_PROGRESS",
              "ON_HOLD",
              "REJECTED",
              "ANSWERED"
            ]
          },
          "inquiredAt": {
            "type": "string",
            "format": "date-time"
          },
          "answerContent": {
            "type": "string"
          },
          "answeredByName": {
            "type": "string"
          },
          "csType": {
            "type": "string"
          }
        }
      },
      "ArtistCreateRequest": {
        "type": "object",
        "properties": {
          "nameKo": {
            "type": "string",
            "minLength": 1
          },
          "nameEn": {
            "type": "string",
            "minLength": 1
          },
          "groupType": {
            "type": "string",
            "enum": [
              "MALE_GROUP",
              "FEMALE_GROUP",
              "MALE_SOLO",
              "FEMALE_SOLO",
              "MIXED",
              "OTHER"
            ],
            "minLength": 1
          }
        },
        "required": [
          "groupType",
          "nameEn",
          "nameKo"
        ]
      },
      "ArtistResponse": {
        "type": "object",
        "properties": {
          "artistId": {
            "type": "integer",
            "format": "int32"
          },
          "nameKo": {
            "type": "string"
          },
          "nameEn": {
            "type": "string"
          },
          "status": {
            "type": "string"
          }
        }
      },
      "InspectionApiResponseArtistResponse": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "$ref": "#/components/schemas/ArtistResponse"
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "AuctionRequest": {
        "type": "object",
        "description": "경매 생성 및 수정을 위한 요청 DTO.",
        "properties": {
          "productInspectionId": {
            "type": "integer",
            "format": "int32"
          },
          "saleType": {
            "type": "string",
            "enum": [
              "AUCTION",
              "INSTANT_BUY"
            ]
          },
          "startPrice": {
            "type": "integer",
            "format": "int32",
            "minimum": 100
          },
          "startTime": {
            "type": "string",
            "format": "date-time"
          },
          "endTime": {
            "type": "string",
            "format": "date-time"
          }
        },
        "required": [
          "endTime",
          "productInspectionId",
          "saleType",
          "startPrice",
          "startTime"
        ]
      },
      "SettlementSettingRequest": {
        "type": "object",
        "description": "정산 설정 생성 및 수정 요청 DTO.",
        "properties": {
          "commissionRate": {
            "type": "number",
            "maximum": 100,
            "minimum": 0
          },
          "settlementCycleType": {
            "type": "string",
            "enum": [
              "DAILY",
              "WEEKLY",
              "MONTHLY",
              "END_OF_MONTH"
            ]
          },
          "settlementDay": {
            "type": "integer",
            "format": "int32"
          }
        },
        "required": [
          "commissionRate",
          "settlementCycleType"
        ]
      },
      "SettlementSettingResponse": {
        "type": "object",
        "description": "정산 설정 조회 응답 DTO.",
        "properties": {
          "settlementSettingId": {
            "type": "integer",
            "format": "int32"
          },
          "commissionRate": {
            "type": "number"
          },
          "settlementCycleType": {
            "type": "string",
            "enum": [
              "DAILY",
              "WEEKLY",
              "MONTHLY",
              "END_OF_MONTH"
            ]
          },
          "settlementDay": {
            "type": "integer",
            "format": "int32"
          },
          "createdAt": {
            "type": "string",
            "format": "date-time"
          },
          "createdBy": {
            "type": "string"
          },
          "updatedAt": {
            "type": "string",
            "format": "date-time"
          },
          "updatedBy": {
            "type": "string"
          }
        }
      },
      "ReturnAdminCreateRequest": {
        "type": "object",
        "properties": {
          "orderId": {
            "type": "string",
            "minLength": 1
          },
          "memberId": {
            "type": "integer",
            "format": "int32"
          },
          "reason": {
            "type": "string",
            "enum": [
              "SIMPLE_CHANGE_OF_MIND",
              "PRODUCT_DEFECT",
              "SHIPPING_ERROR",
              "ETC"
            ]
          },
          "detailReason": {
            "type": "string"
          }
        },
        "required": [
          "memberId",
          "orderId",
          "reason"
        ]
      },
      "ReturnAdminResponse": {
        "type": "object",
        "description": "관리자에게 보여줄 환불/반품 요청의 모든 상세 정보를 담는 DTO입니다.\n \u003Cp\u003E사용자 정보, 처리자 정보, 내부 메모 등 관리자에게만 필요한 정보들을 포함합니다.",
        "properties": {
          "returnRequestId": {
            "type": "integer",
            "format": "int32"
          },
          "orderId": {
            "type": "integer",
            "format": "int32"
          },
          "buyerName": {
            "type": "string"
          },
          "createdBy": {
            "type": "string"
          },
          "updatedBy": {
            "type": "string"
          },
          "reason": {
            "type": "string",
            "enum": [
              "SIMPLE_CHANGE_OF_MIND",
              "PRODUCT_DEFECT",
              "SHIPPING_ERROR",
              "ETC"
            ]
          },
          "detailReason": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "REQUESTED",
              "IN_TRANSIT",
              "INSPECTING",
              "APPROVED",
              "REJECTED",
              "COMPLETED",
              "USER_CANCELLED",
              "DELETED"
            ]
          },
          "originalPaymentAmount": {
            "type": "number"
          },
          "deductedShippingFee": {
            "type": "number"
          },
          "finalRefundAmount": {
            "type": "number"
          },
          "rejectReason": {
            "type": "string"
          },
          "comment": {
            "type": "string"
          },
          "requestedAt": {
            "type": "string",
            "format": "date-time"
          },
          "completedAt": {
            "type": "string",
            "format": "date-time"
          },
          "attachmentUrls": {
            "type": "array",
            "items": {
              "type": "string"
            }
          },
          "statusHistories": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ReturnHistoryResponse"
            }
          }
        }
      },
      "InspectorAnswerDto": {
        "type": "object",
        "properties": {
          "itemKey": {
            "type": "string"
          },
          "answerValue": {
            "type": "string"
          },
          "note": {
            "type": "string"
          }
        }
      },
      "OfflineInspectionRejectRequest": {
        "type": "object",
        "properties": {
          "rejectionReason": {
            "type": "string",
            "minLength": 1
          },
          "inspectorAnswers": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/InspectorAnswerDto"
            }
          }
        },
        "required": [
          "inspectorAnswers",
          "rejectionReason"
        ]
      },
      "OfflineInspectionApproveRequest": {
        "type": "object",
        "properties": {
          "inspectorAnswers": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/InspectorAnswerDto"
            }
          },
          "inspectionNotes": {
            "type": "string"
          },
          "finalBuyPrice": {
            "type": "number"
          },
          "expectedPrice": {
            "type": "number"
          },
          "marketAvgPrice": {
            "type": "number"
          },
          "priceDeductionReason": {
            "type": "string"
          }
        },
        "required": [
          "finalBuyPrice",
          "inspectorAnswers"
        ]
      },
      "InspectionRejectRequest": {
        "type": "object",
        "properties": {
          "rejectionReason": {
            "type": "string",
            "minLength": 1
          }
        },
        "required": [
          "rejectionReason"
        ]
      },
      "NoticeCreateRequest": {
        "type": "object",
        "description": "관리자가 새로운 공지사항을 생성할 때 사용하는 요청 DTO입니다.",
        "properties": {
          "csTypeId": {
            "type": "integer",
            "format": "int32"
          },
          "title": {
            "type": "string",
            "minLength": 1
          },
          "content": {
            "type": "string",
            "minLength": 1
          }
        },
        "required": [
          "content",
          "csTypeId",
          "title"
        ]
      },
      "NoticeDetailResponse": {
        "type": "object",
        "description": "공지사항 상세 조회 시 사용될 응답 DTO입니다. (스마트에디터 내용 포함)",
        "properties": {
          "noticeId": {
            "type": "integer",
            "format": "int32"
          },
          "csType": {
            "type": "string"
          },
          "title": {
            "type": "string"
          },
          "content": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "DRAFT",
              "ACTIVE",
              "PINNED",
              "INACTIVE"
            ]
          },
          "createdBy": {
            "type": "string"
          },
          "createdAt": {
            "type": "string",
            "format": "date-time"
          },
          "updatedBy": {
            "type": "string"
          },
          "updatedAt": {
            "type": "string",
            "format": "date-time"
          },
          "attachmentUrls": {
            "type": "array",
            "items": {
              "type": "string"
            }
          }
        }
      },
      "FaqCreateRequest": {
        "type": "object",
        "properties": {
          "csTypeId": {
            "type": "integer",
            "format": "int32"
          },
          "title": {
            "type": "string"
          },
          "content": {
            "type": "string"
          }
        }
      },
      "FaqResponse": {
        "type": "object",
        "properties": {
          "faqId": {
            "type": "integer",
            "format": "int32"
          },
          "csType": {
            "type": "string"
          },
          "title": {
            "type": "string"
          },
          "content": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "DRAFT",
              "ACTIVE",
              "PINNED",
              "INACTIVE"
            ]
          },
          "createdBy": {
            "type": "string"
          },
          "createdAt": {
            "type": "string",
            "format": "date-time"
          },
          "modifiedBy": {
            "type": "string"
          },
          "updatedAt": {
            "type": "string",
            "format": "date-time"
          },
          "attachmentUrls": {
            "type": "array",
            "items": {
              "type": "string"
            }
          }
        }
      },
      "ReturnAdminUpdateRequest": {
        "type": "object",
        "properties": {
          "status": {
            "type": "string",
            "enum": [
              "REQUESTED",
              "IN_TRANSIT",
              "INSPECTING",
              "APPROVED",
              "REJECTED",
              "COMPLETED",
              "USER_CANCELLED",
              "DELETED"
            ]
          },
          "deductedShippingFee": {
            "type": "number"
          },
          "rejectReason": {
            "type": "string"
          },
          "memo": {
            "type": "string"
          }
        },
        "required": [
          "status"
        ]
      },
      "NoticeUpdateRequest": {
        "type": "object",
        "description": "관리자가 기존 공지사항을 수정할 때 사용하는 요청 DTO입니다.",
        "properties": {
          "csTypeId": {
            "type": "integer",
            "format": "int32"
          },
          "title": {
            "type": "string",
            "minLength": 1
          },
          "content": {
            "type": "string",
            "minLength": 1
          },
          "status": {
            "type": "string",
            "enum": [
              "DRAFT",
              "ACTIVE",
              "PINNED",
              "INACTIVE"
            ]
          }
        },
        "required": [
          "content",
          "csTypeId",
          "title"
        ]
      },
      "InquiryAnswerRequest": {
        "type": "object",
        "properties": {
          "answerContent": {
            "type": "string",
            "minLength": 1
          },
          "reqStatus": {
            "type": "string",
            "enum": [
              "PENDING",
              "IN_PROGRESS",
              "ON_HOLD",
              "REJECTED",
              "ANSWERED"
            ]
          },
          "comment": {
            "type": "string"
          }
        },
        "required": [
          "answerContent"
        ]
      },
      "CsType": {
        "type": "object",
        "properties": {
          "csTypeId": {
            "type": "integer",
            "format": "int32"
          },
          "name": {
            "type": "string"
          }
        }
      },
      "InquiryDetailAdminResponse": {
        "type": "object",
        "properties": {
          "inquiryId": {
            "type": "integer",
            "format": "int32"
          },
          "title": {
            "type": "string"
          },
          "content": {
            "type": "string"
          },
          "inquiredByName": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "PENDING",
              "IN_PROGRESS",
              "ON_HOLD",
              "REJECTED",
              "ANSWERED"
            ]
          },
          "inquiredAt": {
            "type": "string",
            "format": "date-time"
          },
          "answerContent": {
            "type": "string"
          },
          "answeredByName": {
            "type": "string"
          },
          "answeredAt": {
            "type": "string",
            "format": "date-time"
          },
          "attachmentUrls": {
            "type": "array",
            "items": {
              "type": "string"
            }
          },
          "comment": {
            "type": "string"
          },
          "csType": {
            "$ref": "#/components/schemas/CsType"
          }
        }
      },
      "FaqUpdateRequest": {
        "type": "object",
        "properties": {
          "csTypeId": {
            "type": "integer",
            "format": "int32"
          },
          "title": {
            "type": "string"
          },
          "content": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "DRAFT",
              "ACTIVE",
              "PINNED",
              "INACTIVE"
            ]
          }
        }
      },
      "Pageable": {
        "type": "object",
        "properties": {
          "page": {
            "type": "integer",
            "format": "int32",
            "minimum": 0
          },
          "size": {
            "type": "integer",
            "format": "int32",
            "minimum": 1
          },
          "sort": {
            "type": "array",
            "items": {
              "type": "string"
            }
          }
        }
      },
      "PageReturnSummaryResponse": {
        "type": "object",
        "properties": {
          "totalPages": {
            "type": "integer",
            "format": "int32"
          },
          "totalElements": {
            "type": "integer",
            "format": "int64"
          },
          "size": {
            "type": "integer",
            "format": "int32"
          },
          "content": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ReturnSummaryResponse"
            }
          },
          "number": {
            "type": "integer",
            "format": "int32"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "first": {
            "type": "boolean"
          },
          "last": {
            "type": "boolean"
          },
          "numberOfElements": {
            "type": "integer",
            "format": "int32"
          },
          "pageable": {
            "$ref": "#/components/schemas/PageableObject"
          },
          "empty": {
            "type": "boolean"
          }
        }
      },
      "PageableObject": {
        "type": "object",
        "properties": {
          "offset": {
            "type": "integer",
            "format": "int64"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "paged": {
            "type": "boolean"
          },
          "pageSize": {
            "type": "integer",
            "format": "int32"
          },
          "pageNumber": {
            "type": "integer",
            "format": "int32"
          },
          "unpaged": {
            "type": "boolean"
          }
        }
      },
      "ReturnSummaryResponse": {
        "type": "object",
        "description": "환불/반품 요청 목록 조회 시, 각 항목의 핵심 정보를 나타내는 요약 DTO입니다.\n \u003Cp\u003E상세 정보 없이 목록 표시에 필요한 최소한의 정보만을 포함합니다.",
        "properties": {
          "returnRequestId": {
            "type": "integer",
            "format": "int32"
          },
          "orderId": {
            "type": "integer",
            "format": "int32"
          },
          "buyerName": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "REQUESTED",
              "IN_TRANSIT",
              "INSPECTING",
              "APPROVED",
              "REJECTED",
              "COMPLETED",
              "USER_CANCELLED",
              "DELETED"
            ]
          },
          "requestedAt": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "SortObject": {
        "type": "object",
        "properties": {
          "empty": {
            "type": "boolean"
          },
          "sorted": {
            "type": "boolean"
          },
          "unsorted": {
            "type": "boolean"
          }
        }
      },
      "InspectionApiResponseMarketAvgPriceResponse": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "$ref": "#/components/schemas/MarketAvgPriceResponse"
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "MarketAvgPriceResponse": {
        "type": "object",
        "properties": {
          "marketAvgPrice": {
            "type": "number"
          },
          "count": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "ApiResponsePayment": {
        "type": "object",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "data": {
            "$ref": "#/components/schemas/Payment"
          },
          "errorMessage": {
            "type": "string"
          }
        }
      },
      "Payment": {
        "type": "object",
        "properties": {
          "createdAt": {
            "type": "string",
            "format": "date-time"
          },
          "updatedAt": {
            "type": "string",
            "format": "date-time"
          },
          "paymentId": {
            "type": "integer",
            "format": "int32"
          },
          "receiptId": {
            "type": "string"
          },
          "orderId": {
            "type": "string"
          },
          "price": {
            "type": "integer",
            "format": "int32"
          },
          "cancelledPrice": {
            "type": "integer",
            "format": "int32"
          },
          "orderName": {
            "type": "string"
          },
          "metadata": {
            "type": "string"
          },
          "pg": {
            "type": "string"
          },
          "method": {
            "type": "string"
          },
          "currency": {
            "type": "string"
          },
          "requestedAt": {
            "type": "string",
            "format": "date-time"
          },
          "purchasedAt": {
            "type": "string",
            "format": "date-time"
          },
          "cancelledAt": {
            "type": "string",
            "format": "date-time"
          },
          "receiptUrl": {
            "type": "string"
          },
          "bootpayStatus": {
            "type": "string",
            "enum": [
              "CASH_RECEIPT_CANCEL_FAILED",
              "CASH_RECEIPT_ISSUE_FAILED",
              "AUTO_BILLING_KEY_FAILED",
              "CLOSE_PAYMENT",
              "AUTO_BILLING_KEY_CANCELLED",
              "PAYMENT_APPROVAL_FAILED",
              "VIRTUAL_ACCOUNT_CANCELLED",
              "PAYMENT_REQUEST_FAILED",
              "PAYMENT_WAITING",
              "PAYMENT_COMPLETED",
              "PAYMENT_APPROVING",
              "PG_APPROVAL_REQUEST",
              "VIRTUAL_ACCOUNT_ISSUED",
              "AUTO_BILLING_KEY_COMPLETED",
              "IDENTITY_VERIFICATION_COMPLETED",
              "PAYMENT_CANCELLED",
              "AUTO_BILLING_KEY_READY",
              "AUTO_BILLING_KEY_BEFORE",
              "AUTO_BILLING_KEY_SUCCESS",
              "IDENTITY_VERIFICATION_READY",
              "CASH_RECEIPT_ISSUED",
              "CASH_RECEIPT_CANCELLED"
            ]
          },
          "status": {
            "type": "string",
            "enum": [
              "VERIFYING",
              "COMPLETE",
              "FORGED",
              "CANCELLED",
              "VOID"
            ]
          },
          "paymentInfo": {
            "type": "object",
            "additionalProperties": {

            }
          },
          "version": {
            "type": "integer",
            "format": "int64"
          },
          "paymentWaiting": {
            "type": "boolean"
          },
          "paymentApproving": {
            "type": "boolean"
          }
        }
      },
      "PageSettlementSummaryResponse": {
        "type": "object",
        "properties": {
          "totalPages": {
            "type": "integer",
            "format": "int32"
          },
          "totalElements": {
            "type": "integer",
            "format": "int64"
          },
          "size": {
            "type": "integer",
            "format": "int32"
          },
          "content": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/SettlementSummaryResponse"
            }
          },
          "number": {
            "type": "integer",
            "format": "int32"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "first": {
            "type": "boolean"
          },
          "last": {
            "type": "boolean"
          },
          "numberOfElements": {
            "type": "integer",
            "format": "int32"
          },
          "pageable": {
            "$ref": "#/components/schemas/PageableObject"
          },
          "empty": {
            "type": "boolean"
          }
        }
      },
      "SettlementSummaryResponse": {
        "type": "object",
        "description": "정산 내역 목록 조회의 응답 DTO.",
        "properties": {
          "settlementId": {
            "type": "integer",
            "format": "int32"
          },
          "sellerName": {
            "type": "string"
          },
          "settlementAmount": {
            "type": "number"
          },
          "status": {
            "type": "string",
            "enum": [
              "PENDING",
              "PAID",
              "CANCELLED",
              "FAILED"
            ]
          },
          "requestedAt": {
            "type": "string",
            "format": "date-time"
          },
          "completedAt": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "SettlementDetailResponse": {
        "type": "object",
        "description": "정산 내역 상세 조회의 응답 DTO.",
        "properties": {
          "settlementId": {
            "type": "integer",
            "format": "int32"
          },
          "sellerName": {
            "type": "string"
          },
          "totalAmount": {
            "type": "number"
          },
          "commissionAmount": {
            "type": "number"
          },
          "settlementAmount": {
            "type": "number"
          },
          "status": {
            "type": "string",
            "enum": [
              "PENDING",
              "PAID",
              "CANCELLED",
              "FAILED"
            ]
          },
          "requestedAt": {
            "type": "string",
            "format": "date-time"
          },
          "completedAt": {
            "type": "string",
            "format": "date-time"
          },
          "failureReason": {
            "type": "string"
          },
          "bankName": {
            "type": "string"
          },
          "accountNumber": {
            "type": "string"
          },
          "items": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/SettlementItemDto"
            }
          }
        }
      },
      "SettlementItemDto": {
        "type": "object",
        "description": "정산 상세 내역에 포함되는 개별 정산 항목 DTO.",
        "properties": {
          "orderId": {
            "type": "integer",
            "format": "int32"
          },
          "productName": {
            "type": "string"
          },
          "itemSaleAmount": {
            "type": "number"
          },
          "commissionRate": {
            "type": "number"
          },
          "commissionAmount": {
            "type": "number"
          },
          "totalAmount": {
            "type": "number"
          },
          "isReturned": {
            "type": "boolean"
          }
        }
      },
      "InspectionApiResponseListMyInspectionResponse": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/MyInspectionResponse"
            }
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "MyInspectionResponse": {
        "type": "object",
        "properties": {
          "productInspectionId": {
            "type": "integer",
            "format": "int32"
          },
          "itemName": {
            "type": "string"
          },
          "goodsCategoryName": {
            "type": "string"
          },
          "artistName": {
            "type": "string"
          },
          "sellerHopePrice": {
            "type": "number"
          },
          "inspectionStatus": {
            "type": "string",
            "enum": [
              "DRAFT",
              "SUBMITTED",
              "ONLINE_APPROVED",
              "ONLINE_REJECTED",
              "OFFLINE_INSPECTING",
              "OFFLINE_REJECTED",
              "COMPLETED"
            ]
          },
          "submittedAt": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "NoticeSearchRequest": {
        "type": "object",
        "properties": {
          "csTypeId": {
            "type": "integer",
            "format": "int32"
          },
          "keyword": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "DRAFT",
              "ACTIVE",
              "PINNED",
              "INACTIVE"
            ]
          }
        }
      },
      "NoticeSummaryResponse": {
        "type": "object",
        "description": "공지사항 목록 조회 시 사용될 가벼운 응답 DTO입니다. (상세 내용 제외)",
        "properties": {
          "noticeId": {
            "type": "integer",
            "format": "int32"
          },
          "csType": {
            "type": "string"
          },
          "title": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "DRAFT",
              "ACTIVE",
              "PINNED",
              "INACTIVE"
            ]
          },
          "createdBy": {
            "type": "string"
          },
          "createdAt": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "PageNoticeSummaryResponse": {
        "type": "object",
        "properties": {
          "totalPages": {
            "type": "integer",
            "format": "int32"
          },
          "totalElements": {
            "type": "integer",
            "format": "int64"
          },
          "size": {
            "type": "integer",
            "format": "int32"
          },
          "content": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/NoticeSummaryResponse"
            }
          },
          "number": {
            "type": "integer",
            "format": "int32"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "first": {
            "type": "boolean"
          },
          "last": {
            "type": "boolean"
          },
          "numberOfElements": {
            "type": "integer",
            "format": "int32"
          },
          "pageable": {
            "$ref": "#/components/schemas/PageableObject"
          },
          "empty": {
            "type": "boolean"
          }
        }
      },
      "PageInquirySummaryResponse": {
        "type": "object",
        "properties": {
          "totalPages": {
            "type": "integer",
            "format": "int32"
          },
          "totalElements": {
            "type": "integer",
            "format": "int64"
          },
          "size": {
            "type": "integer",
            "format": "int32"
          },
          "content": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/InquirySummaryResponse"
            }
          },
          "number": {
            "type": "integer",
            "format": "int32"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "first": {
            "type": "boolean"
          },
          "last": {
            "type": "boolean"
          },
          "numberOfElements": {
            "type": "integer",
            "format": "int32"
          },
          "pageable": {
            "$ref": "#/components/schemas/PageableObject"
          },
          "empty": {
            "type": "boolean"
          }
        }
      },
      "InquiryDetailUserResponse": {
        "type": "object",
        "properties": {
          "inquiryId": {
            "type": "integer",
            "format": "int32"
          },
          "title": {
            "type": "string"
          },
          "content": {
            "type": "string"
          },
          "inquiredByName": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "PENDING",
              "IN_PROGRESS",
              "ON_HOLD",
              "REJECTED",
              "ANSWERED"
            ]
          },
          "inquiredAt": {
            "type": "string",
            "format": "date-time"
          },
          "answerContent": {
            "type": "string"
          },
          "answeredByName": {
            "type": "string"
          },
          "answeredAt": {
            "type": "string",
            "format": "date-time"
          },
          "attachmentUrls": {
            "type": "array",
            "items": {
              "type": "string"
            }
          }
        }
      },
      "FaqSearchRequest": {
        "type": "object",
        "properties": {
          "csTypeId": {
            "type": "integer",
            "format": "int32"
          },
          "keyword": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "DRAFT",
              "ACTIVE",
              "PINNED",
              "INACTIVE"
            ]
          }
        }
      },
      "PageFaqResponse": {
        "type": "object",
        "properties": {
          "totalPages": {
            "type": "integer",
            "format": "int32"
          },
          "totalElements": {
            "type": "integer",
            "format": "int64"
          },
          "size": {
            "type": "integer",
            "format": "int32"
          },
          "content": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/FaqResponse"
            }
          },
          "number": {
            "type": "integer",
            "format": "int32"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "first": {
            "type": "boolean"
          },
          "last": {
            "type": "boolean"
          },
          "numberOfElements": {
            "type": "integer",
            "format": "int32"
          },
          "pageable": {
            "$ref": "#/components/schemas/PageableObject"
          },
          "empty": {
            "type": "boolean"
          }
        }
      },
      "InspectionApiResponseOnlineChecklistResponse": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "$ref": "#/components/schemas/OnlineChecklistResponse"
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "OnlineChecklistItemResponse": {
        "type": "object",
        "properties": {
          "checklistItemId": {
            "type": "integer",
            "format": "int32"
          },
          "itemKey": {
            "type": "string"
          },
          "label": {
            "type": "string"
          },
          "type": {
            "type": "string"
          },
          "options": {
            "type": "string"
          },
          "required": {
            "type": "boolean"
          },
          "orderIndex": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "OnlineChecklistResponse": {
        "type": "object",
        "properties": {
          "templateId": {
            "type": "integer",
            "format": "int32"
          },
          "templateVersion": {
            "type": "integer",
            "format": "int32"
          },
          "items": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/OnlineChecklistItemResponse"
            }
          }
        }
      },
      "GoodsCategoryResponse": {
        "type": "object",
        "properties": {
          "GoodsCategoryId": {
            "type": "integer",
            "format": "int32"
          },
          "code": {
            "type": "string"
          },
          "name": {
            "type": "string"
          }
        }
      },
      "InspectionApiResponseListGoodsCategoryResponse": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/GoodsCategoryResponse"
            }
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "InspectionApiResponseListArtistResponse": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ArtistResponse"
            }
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "AlbumResponse": {
        "type": "object",
        "properties": {
          "albumId": {
            "type": "integer",
            "format": "int32"
          },
          "artist": {
            "$ref": "#/components/schemas/ArtistResponse"
          },
          "title": {
            "type": "string"
          },
          "releaseDate": {
            "type": "string",
            "format": "date"
          },
          "version": {
            "type": "string"
          }
        }
      },
      "InspectionApiResponseListAlbumResponse": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/AlbumResponse"
            }
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "Bid": {
        "type": "object",
        "properties": {
          "bidId": {
            "type": "integer",
            "format": "int32"
          },
          "bidAmount": {
            "type": "integer",
            "format": "int32"
          },
          "bidAt": {
            "type": "string",
            "format": "date-time"
          },
          "bidderName": {
            "type": "string"
          },
          "bidderId": {
            "type": "integer",
            "format": "int32"
          },
          "itemName": {
            "type": "string"
          },
          "itemId": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "AuctionSummaryResponse": {
        "type": "object",
        "properties": {
          "auctionId": {
            "type": "integer",
            "format": "int32"
          },
          "itemName": {
            "type": "string"
          },
          "startPrice": {
            "type": "integer",
            "format": "int32"
          },
          "currentPrice": {
            "type": "integer",
            "format": "int32"
          },
          "startTime": {
            "type": "string",
            "format": "date-time"
          },
          "endTime": {
            "type": "string",
            "format": "date-time"
          },
          "saleType": {
            "type": "string"
          },
          "saleStatus": {
            "type": "string"
          }
        }
      },
      "PageAuctionSummaryResponse": {
        "type": "object",
        "properties": {
          "totalPages": {
            "type": "integer",
            "format": "int32"
          },
          "totalElements": {
            "type": "integer",
            "format": "int64"
          },
          "size": {
            "type": "integer",
            "format": "int32"
          },
          "content": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/AuctionSummaryResponse"
            }
          },
          "number": {
            "type": "integer",
            "format": "int32"
          },
          "sort": {
            "$ref": "#/components/schemas/SortObject"
          },
          "first": {
            "type": "boolean"
          },
          "last": {
            "type": "boolean"
          },
          "numberOfElements": {
            "type": "integer",
            "format": "int32"
          },
          "pageable": {
            "$ref": "#/components/schemas/PageableObject"
          },
          "empty": {
            "type": "boolean"
          }
        }
      },
      "Auction": {
        "type": "object",
        "properties": {
          "auctionId": {
            "type": "integer",
            "format": "int32"
          },
          "saleType": {
            "type": "string",
            "enum": [
              "AUCTION",
              "INSTANT_BUY"
            ]
          },
          "saleStatus": {
            "type": "string",
            "enum": [
              "PREPARING",
              "ACTIVE",
              "SOLD",
              "NOT_SOLD",
              "CANCELLED"
            ]
          },
          "startPrice": {
            "type": "integer",
            "format": "int32"
          },
          "finalPrice": {
            "type": "integer",
            "format": "int32"
          },
          "startTime": {
            "type": "string",
            "format": "date-time"
          },
          "endTime": {
            "type": "string",
            "format": "date-time"
          },
          "createdAt": {
            "type": "string",
            "format": "date-time"
          },
          "updatedAt": {
            "type": "string",
            "format": "date-time"
          },
          "productInspection": {
            "$ref": "#/components/schemas/ProductInspection"
          }
        }
      },
      "InspectionFile": {
        "type": "object",
        "properties": {
          "inspectionFileId": {
            "type": "integer",
            "format": "int32"
          },
          "productInspection": {

          },
          "fileMeta": {
            "$ref": "#/components/schemas/FileMeta"
          },
          "createdAt": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "ProductInspection": {
        "type": "object",
        "properties": {
          "productInspectionId": {
            "type": "integer",
            "format": "int32"
          },
          "submissionUuid": {
            "type": "string"
          },
          "memberId": {
            "type": "integer",
            "format": "int32"
          },
          "templateId": {
            "type": "integer",
            "format": "int32"
          },
          "templateVersion": {
            "type": "integer",
            "format": "int32"
          },
          "firstInspectorId": {
            "type": "integer",
            "format": "int32"
          },
          "secondInspectorId": {
            "type": "integer",
            "format": "int32"
          },
          "goodsCategoryId": {
            "type": "integer",
            "format": "int32"
          },
          "artistId": {
            "type": "integer",
            "format": "int32"
          },
          "albumId": {
            "type": "integer",
            "format": "int32"
          },
          "itemName": {
            "type": "string"
          },
          "itemDescription": {
            "type": "string"
          },
          "hashtags": {
            "type": "string"
          },
          "expectedPrice": {
            "type": "number"
          },
          "marketAvgPrice": {
            "type": "number"
          },
          "sellerHopePrice": {
            "type": "number"
          },
          "finalBuyPrice": {
            "type": "number"
          },
          "shippingAddress": {
            "type": "string"
          },
          "shippingAddressDetail": {
            "type": "string"
          },
          "bankName": {
            "type": "string"
          },
          "bankAccount": {
            "type": "string"
          },
          "inspectionStatus": {
            "type": "string",
            "enum": [
              "DRAFT",
              "SUBMITTED",
              "ONLINE_APPROVED",
              "ONLINE_REJECTED",
              "OFFLINE_INSPECTING",
              "OFFLINE_REJECTED",
              "COMPLETED"
            ]
          },
          "inventoryStatus": {
            "type": "string",
            "enum": [
              "PENDING",
              "ACTIVE",
              "SOLD",
              "CANCELED"
            ]
          },
          "sourceType": {
            "type": "string",
            "enum": [
              "CONSIGNMENT",
              "PURCHASED"
            ]
          },
          "finalGrade": {
            "type": "string",
            "enum": [
              "A",
              "B",
              "C"
            ]
          },
          "firstRejectionReason": {
            "type": "string"
          },
          "secondRejectionReason": {
            "type": "string"
          },
          "inspectionSummary": {
            "type": "string"
          },
          "priceDeductionReason": {
            "type": "string"
          },
          "inspectionNotes": {
            "type": "string"
          },
          "createdAt": {
            "type": "string",
            "format": "date-time"
          },
          "submittedAt": {
            "type": "string",
            "format": "date-time"
          },
          "onlineInspectedAt": {
            "type": "string",
            "format": "date-time"
          },
          "offlineInspectedAt": {
            "type": "string",
            "format": "date-time"
          },
          "completedAt": {
            "type": "string",
            "format": "date-time"
          },
          "updatedAt": {
            "type": "string",
            "format": "date-time"
          },
          "files": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/InspectionFile"
            }
          }
        }
      },
      "SettlementSearchCondition": {
        "type": "object",
        "description": "정산 내역 목록 조회를 위한 동적 검색 조건을 담는 DTO.",
        "properties": {
          "sellerName": {
            "type": "string"
          },
          "status": {
            "type": "string",
            "enum": [
              "PENDING",
              "PAID",
              "CANCELLED",
              "FAILED"
            ]
          },
          "startDate": {
            "type": "string",
            "format": "date"
          },
          "endDate": {
            "type": "string",
            "format": "date"
          }
        }
      },
      "SettlementStatsResponse": {
        "type": "object",
        "description": "정산 상태별 개수 통계 DTO",
        "properties": {
          "totalCount": {
            "type": "integer",
            "format": "int64"
          },
          "pendingCount": {
            "type": "integer",
            "format": "int64"
          },
          "paidCount": {
            "type": "integer",
            "format": "int64"
          },
          "cancelledCount": {
            "type": "integer",
            "format": "int64"
          },
          "failedCount": {
            "type": "integer",
            "format": "int64"
          }
        }
      },
      "SettlementDashboardResponse": {
        "type": "object",
        "description": "관리자 대시보드에 표시될 정산 관련 핵심 지표(KPI) DTO.",
        "properties": {
          "monthlyScheduledAmount": {
            "type": "number"
          },
          "yesterdaySettledAmount": {
            "type": "number"
          },
          "pendingOrFailedCount": {
            "type": "integer",
            "format": "int64"
          },
          "cumulativeSettlementAmount": {
            "type": "number"
          }
        }
      },
      "ReturnSearchRequest": {
        "type": "object",
        "description": "환불 목록 동적 검색을 위한 조건을 담는 DTO입니다.",
        "properties": {
          "status": {
            "type": "string",
            "enum": [
              "REQUESTED",
              "IN_TRANSIT",
              "INSPECTING",
              "APPROVED",
              "REJECTED",
              "COMPLETED",
              "USER_CANCELLED",
              "DELETED"
            ]
          },
          "buyerName": {
            "type": "string"
          }
        }
      },
      "ReturnStatsResponse": {
        "type": "object",
        "description": "환불/반품 상태별 개수 통계 DTO",
        "properties": {
          "totalCount": {
            "type": "integer",
            "format": "int64"
          },
          "requestedCount": {
            "type": "integer",
            "format": "int64"
          },
          "inTransitCount": {
            "type": "integer",
            "format": "int64"
          },
          "inspectingCount": {
            "type": "integer",
            "format": "int64"
          },
          "approvedCount": {
            "type": "integer",
            "format": "int64"
          },
          "rejectedCount": {
            "type": "integer",
            "format": "int64"
          },
          "completedCount": {
            "type": "integer",
            "format": "int64"
          },
          "userCancelledCount": {
            "type": "integer",
            "format": "int64"
          }
        }
      },
      "InspectionApiResponseInspectionPageResponseInspectionListResponse": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "$ref": "#/components/schemas/InspectionPageResponseInspectionListResponse"
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "InspectionListResponse": {
        "type": "object",
        "description": "FE에서 관리자 페이지의 검수 리스트 조회 응답 DTO",
        "properties": {
          "productInspectionId": {
            "type": "integer",
            "format": "int32"
          },
          "submissionUuid": {
            "type": "string"
          },
          "memberId": {
            "type": "integer",
            "format": "int32"
          },
          "memberName": {
            "type": "string"
          },
          "goodsCategoryId": {
            "type": "integer",
            "format": "int32"
          },
          "goodsCategoryName": {
            "type": "string"
          },
          "artistId": {
            "type": "integer",
            "format": "int32"
          },
          "artistName": {
            "type": "string"
          },
          "albumId": {
            "type": "integer",
            "format": "int32"
          },
          "albumTitle": {
            "type": "string"
          },
          "itemName": {
            "type": "string"
          },
          "expectedPrice": {
            "type": "number"
          },
          "marketAvgPrice": {
            "type": "number"
          },
          "sellerHopePrice": {
            "type": "number"
          },
          "submittedAt": {
            "type": "string",
            "format": "date-time"
          },
          "inspectionStatus": {
            "type": "string",
            "enum": [
              "DRAFT",
              "SUBMITTED",
              "ONLINE_APPROVED",
              "ONLINE_REJECTED",
              "OFFLINE_INSPECTING",
              "OFFLINE_REJECTED",
              "COMPLETED"
            ]
          }
        }
      },
      "InspectionPageResponseInspectionListResponse": {
        "type": "object",
        "description": "FE가 테이블/페이지네이션에서 공통으로 쓰는 표준 페이지 응답 형태\n - items: 실제 데이터 배열",
        "properties": {
          "items": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/InspectionListResponse"
            }
          },
          "meta": {
            "$ref": "#/components/schemas/Meta"
          }
        }
      },
      "Meta": {
        "type": "object",
        "properties": {
          "page": {
            "type": "integer",
            "format": "int32"
          },
          "size": {
            "type": "integer",
            "format": "int32"
          },
          "totalElements": {
            "type": "integer",
            "format": "int64"
          },
          "totalPages": {
            "type": "integer",
            "format": "int32"
          }
        }
      },
      "ChecklistAnswerInfo": {
        "type": "object",
        "properties": {
          "itemKey": {
            "type": "string"
          },
          "itemLabel": {
            "type": "string"
          },
          "answerValue": {
            "type": "string"
          },
          "note": {
            "type": "string"
          }
        }
      },
      "FileInfo": {
        "type": "object",
        "properties": {
          "fileId": {
            "type": "integer",
            "format": "int32"
          },
          "fileUrl": {
            "type": "string"
          },
          "fileType": {
            "type": "string"
          }
        }
      },
      "InspectionApiResponseOnlineInspectionDetailResponse": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "$ref": "#/components/schemas/OnlineInspectionDetailResponse"
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "OnlineInspectionDetailResponse": {
        "type": "object",
        "description": "FE에서 관리자 페이지의 1차 온라인 검수 상세 조회 응답 DTO",
        "properties": {
          "productInspectionId": {
            "type": "integer",
            "format": "int32"
          },
          "submissionUuid": {
            "type": "string"
          },
          "inspectionStatus": {
            "type": "string",
            "enum": [
              "DRAFT",
              "SUBMITTED",
              "ONLINE_APPROVED",
              "ONLINE_REJECTED",
              "OFFLINE_INSPECTING",
              "OFFLINE_REJECTED",
              "COMPLETED"
            ]
          },
          "submittedAt": {
            "type": "string",
            "format": "date-time"
          },
          "itemName": {
            "type": "string"
          },
          "itemDescription": {
            "type": "string"
          },
          "hashtags": {
            "type": "string"
          },
          "goodsCategoryId": {
            "type": "integer",
            "format": "int32"
          },
          "goodsCategoryName": {
            "type": "string"
          },
          "artistId": {
            "type": "integer",
            "format": "int32"
          },
          "artistName": {
            "type": "string"
          },
          "albumId": {
            "type": "integer",
            "format": "int32"
          },
          "albumTitle": {
            "type": "string"
          },
          "expectedPrice": {
            "type": "number"
          },
          "marketAvgPrice": {
            "type": "number"
          },
          "sellerHopePrice": {
            "type": "number"
          },
          "seller": {
            "$ref": "#/components/schemas/UserInfo"
          },
          "bankName": {
            "type": "string"
          },
          "bankAccount": {
            "type": "string"
          },
          "shippingAddress": {
            "type": "string"
          },
          "shippingAddressDetail": {
            "type": "string"
          },
          "firstInspector": {
            "$ref": "#/components/schemas/UserInfo"
          },
          "files": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/FileInfo"
            }
          },
          "answers": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/ChecklistAnswerInfo"
            }
          },
          "templateId": {
            "type": "integer",
            "format": "int32"
          },
          "templateVersion": {
            "type": "integer",
            "format": "int32"
          },
          "firstRejectionReason": {
            "type": "string"
          }
        }
      },
      "UserInfo": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int32"
          },
          "name": {
            "type": "string"
          },
          "email": {
            "type": "string"
          },
          "tel": {
            "type": "string"
          }
        }
      },
      "InspectionApiResponseOfflineInspectionDetailResponse": {
        "type": "object",
        "description": "모든 검수 API 성공 포맷 래퍼",
        "properties": {
          "success": {
            "type": "boolean"
          },
          "code": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "data": {
            "$ref": "#/components/schemas/OfflineInspectionDetailResponse"
          },
          "timestamp": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "OfflineChecklistItemResponse": {
        "type": "object",
        "properties": {
          "checklistItem": {
            "$ref": "#/components/schemas/OnlineChecklistItemResponse"
          },
          "sellerAnswer": {
            "type": "string"
          },
          "inspectorAnswer": {
            "type": "string"
          },
          "note": {
            "type": "string"
          }
        }
      },
      "OfflineInspectionDetailResponse": {
        "type": "object",
        "description": "FE에서 관리자 페이지의 2차 오프라인 검수 상세 조회 응답 DTO",
        "properties": {
          "onlineDetail": {
            "$ref": "#/components/schemas/OnlineInspectionDetailResponse"
          },
          "secondInspector": {
            "$ref": "#/components/schemas/UserInfo"
          },
          "checklist": {
            "type": "array",
            "items": {
              "$ref": "#/components/schemas/OfflineChecklistItemResponse"
            }
          },
          "finalBuyPrice": {
            "type": "number"
          },
          "priceDeductionReason": {
            "type": "string"
          },
          "inspectionNotes": {
            "type": "string"
          },
          "secondRejectionReason": {
            "type": "string"
          }
        }
      },
      "DashboardSummaryResponse": {
        "type": "object",
        "description": "관리자 대시보드 전체 요약 정보 응답 DTO",
        "properties": {
          "inquiryStats": {
            "$ref": "#/components/schemas/InquiryStatsAdminResponse",
            "description": "문의 관련 통계"
          },
          "faqStats": {
            "$ref": "#/components/schemas/FaqStatsResponse",
            "description": "FAQ 관련 통계"
          },
          "noticeStats": {
            "$ref": "#/components/schemas/NoticeStatsResponse",
            "description": "공지사항 관련 통계"
          },
          "returnStats": {
            "$ref": "#/components/schemas/ReturnStatsResponse",
            "description": "환불/반품 관련 통계"
          },
          "settlementStats": {
            "$ref": "#/components/schemas/SettlementStatsResponse",
            "description": "정산 관련 통계"
          }
        }
      },
      "FaqStatsResponse": {
        "type": "object",
        "description": "FAQ 상태별 개수 통계 DTO",
        "properties": {
          "totalCount": {
            "type": "integer",
            "format": "int64"
          },
          "draftCount": {
            "type": "integer",
            "format": "int64"
          },
          "activeCount": {
            "type": "integer",
            "format": "int64"
          },
          "pinnedCount": {
            "type": "integer",
            "format": "int64"
          },
          "inactiveCount": {
            "type": "integer",
            "format": "int64"
          }
        }
      },
      "InquiryStatsAdminResponse": {
        "type": "object",
        "description": "관리자용 문의 도메인 상세 통계 응답 DTO",
        "properties": {
          "todayNewCount": {
            "type": "integer",
            "format": "int64",
            "description": "오늘 접수된 문의 건수",
            "example": 5
          },
          "pendingCount": {
            "type": "integer",
            "format": "int64",
            "description": "답변 대기 중인 총 문의 건수",
            "example": 12
          },
          "inProgressCount": {
            "type": "integer",
            "format": "int64",
            "description": "처리 중인 총 문의 건수",
            "example": 3
          },
          "onHoldCount": {
            "type": "integer",
            "format": "int64",
            "description": "보류 중인 총 문의 건수",
            "example": 1
          },
          "answeredCount": {
            "type": "integer",
            "format": "int64",
            "description": "답변 완료된 총 문의 건수",
            "example": 150
          },
          "rejectedCount": {
            "type": "integer",
            "format": "int64",
            "description": "거절된 총 문의 건수",
            "example": 5
          },
          "totalCount": {
            "type": "integer",
            "format": "int64",
            "description": "전체 문의 건수",
            "example": 171
          }
        }
      },
      "NoticeStatsResponse": {
        "type": "object",
        "description": "공지사항 상태별 개수 통계 DTO",
        "properties": {
          "totalCount": {
            "type": "integer",
            "format": "int64"
          },
          "draftCount": {
            "type": "integer",
            "format": "int64"
          },
          "activeCount": {
            "type": "integer",
            "format": "int64"
          },
          "pinnedCount": {
            "type": "integer",
            "format": "int64"
          },
          "inactiveCount": {
            "type": "integer",
            "format": "int64"
          }
        }
      },
      "InquirySearchCondition": {
        "type": "object",
        "description": "1:1 문의 목록 조회를 위한 동적 검색 조건을 담는 DTO입니다.\n 컨트롤러에서 @ModelAttribute를 통해 클라이언트의 쿼리 파라미터를 이 객체에 바인딩합니다.\n 예: /api/inquiries?status=PENDING&memberName=홍길동",
        "properties": {
          "status": {
            "type": "string",
            "description": "검색할 문의 답변 상태 (PENDING, COMPLETED)",
            "enum": [
              "PENDING",
              "IN_PROGRESS",
              "ON_HOLD",
              "REJECTED",
              "ANSWERED"
            ]
          },
          "csTypeId": {
            "type": "integer",
            "format": "int32",
            "description": "검색할 문의 유형 ID (결제, 계정, 기타 등)"
          },
          "memberName": {
            "type": "string",
            "description": "검색할 작성자 이름 (부분 일치)"
          }
        }
      }
    }
  }
}