#!/bin/bash

# Configuration Validation Script
# Verifies that environment variables are properly set

echo "🔍 MCP Marketing Suite - Configuration Validation"
echo "=================================================="
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check function
check_var() {
    local var_name=$1
    local required=$2
    local default_value=$3

    if [ -z "${!var_name}" ]; then
        if [ "$required" = "true" ]; then
            echo -e "${RED}✗${NC} $var_name: ${RED}NOT SET (Required)${NC}"
            return 1
        else
            if [ -n "$default_value" ]; then
                echo -e "${YELLOW}○${NC} $var_name: Not set (will use default: $default_value)"
            else
                echo -e "${YELLOW}○${NC} $var_name: Not set (optional)"
            fi
            return 0
        fi
    else
        # Mask API keys
        if [[ $var_name == *"API_KEY"* ]]; then
            masked_value="${!var_name:0:7}...${!var_name: -4}"
            echo -e "${GREEN}✓${NC} $var_name: ${GREEN}SET${NC} ($masked_value)"
        else
            echo -e "${GREEN}✓${NC} $var_name: ${GREEN}SET${NC} (${!var_name})"
        fi
        return 0
    fi
}

# Check AI agent mode
AI_ENABLED=${ENABLE_AI_AGENTS:-true}

echo "Configuration Mode: ${AI_ENABLED}"
echo ""

# Check variables
errors=0

echo "Core Configuration:"
echo "-------------------"

if [ "$AI_ENABLED" = "true" ]; then
    check_var "OPENAI_API_KEY" "true" "" || ((errors++))
else
    check_var "OPENAI_API_KEY" "false" ""
fi

check_var "LLM_MODEL" "false" "gpt-4"
check_var "ENABLE_AI_AGENTS" "false" "true"
check_var "OUTPUT_DIR" "false" "./outputs"

echo ""
echo "Java Environment:"
echo "-----------------"

# Check Java
if command -v java &> /dev/null; then
    java_version=$(java -version 2>&1 | head -n 1)
    echo -e "${GREEN}✓${NC} Java: ${GREEN}INSTALLED${NC} ($java_version)"
else
    echo -e "${RED}✗${NC} Java: ${RED}NOT FOUND${NC}"
    ((errors++))
fi

# Check Maven
if command -v mvn &> /dev/null; then
    mvn_version=$(mvn -version | head -n 1)
    echo -e "${GREEN}✓${NC} Maven: ${GREEN}INSTALLED${NC} ($mvn_version)"
else
    echo -e "${RED}✗${NC} Maven: ${RED}NOT FOUND${NC}"
    ((errors++))
fi

echo ""
echo "File System:"
echo "------------"

# Check output directory
output_dir=${OUTPUT_DIR:-./outputs}
if [ -d "$output_dir" ]; then
    echo -e "${GREEN}✓${NC} Output directory: ${GREEN}EXISTS${NC} ($output_dir)"
else
    echo -e "${YELLOW}○${NC} Output directory: Will be created ($output_dir)"
fi

# Check application.yml
if [ -f "src/main/resources/application.yml" ]; then
    echo -e "${GREEN}✓${NC} application.yml: ${GREEN}FOUND${NC}"
else
    echo -e "${RED}✗${NC} application.yml: ${RED}NOT FOUND${NC}"
    ((errors++))
fi

echo ""
echo "=================================================="

if [ $errors -eq 0 ]; then
    if [ "$AI_ENABLED" = "true" ] && [ -z "$OPENAI_API_KEY" ]; then
        echo -e "${YELLOW}⚠ Warning:${NC} AI agents enabled but OPENAI_API_KEY not set"
        echo ""
        echo "To set your API key:"
        echo "  export OPENAI_API_KEY=\"sk-your-api-key\""
        echo ""
        echo "Or run in deterministic mode (no AI):"
        echo "  export ENABLE_AI_AGENTS=false"
        exit 1
    else
        echo -e "${GREEN}✓ Configuration is valid!${NC}"
        echo ""
        echo "Ready to run:"
        echo "  mvn spring-boot:run"
        exit 0
    fi
else
    echo -e "${RED}✗ Configuration has errors!${NC}"
    echo ""
    echo "Please fix the issues above and try again."
    echo ""
    echo "For help, see: docs/CONFIGURATION.md"
    exit 1
fi

