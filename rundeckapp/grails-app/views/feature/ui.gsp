%{--
  - Copyright 2019 Rundeck, Inc. (http://rundeck.com)
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <meta name="skipPrototypeJs" content="true"/>
    <meta name="layout" content="base"/>
    <meta name="tabpage" content="Features"/>
    <title><g:appTitle/></title>
    <cfg:setVar var="guiBase" key="gui.basePath" />
    <link rel="stylesheet" href="${guiBase}/feature/css/index.css"/>

    <style type="text/css">
    .dismiss-positioner button.close {
        right: -8px !important;
    }
    </style>
</head>

<body>
<div style="display: flex;height: 100%;">
    <div style="flex-grow: 1">
        <div id=feature-vue></div>
    </div>
</div>

<script type="module" crossorigin src="${guiBase}/feature/index.js"></script>
</body>
</html>