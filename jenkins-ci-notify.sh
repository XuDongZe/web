BUILD_STATUS=$1

text="\"构建Id: ${string2}${string1}\n构建时间: ${nowTime}\n链接: ${JOB_URL}\n\""
msg="{\"msg_type\":\"post\",\"content\": {\"post\": {\"zh_cn\": {\"title\": \"Jenkins Ci \"$STATUS,\"content\": [[{\"tag\": \"text\",\"text\": $text}]]} } }}"

feishu_notify() {
    echo $1

    JOB_URL="${JENKINS_URL}job/${JOB_NAME}/${BUILD_NUMBER}"
    NR=${BUILD_NUMBER}
    string1=$BUILD_DISPLAY_NAME
    string2=$JOB_BASE_NAME
    nowTime=$(date "+%Y-%m-%d %H:%M:%S")

    curl -X POST -H "Content-Type: application/json" -d "$msg" https://open.feishu.cn/open-apis/bot/v2/hook/46bf6110-72d3-4d8e-abd2-3cbdbd933e50
}

feishu_notify $BUILD_STATUS