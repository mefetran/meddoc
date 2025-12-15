#!/bin/bash

TESTS=(
    # User Credentials Tests
    "ValidateUserCredentialsNoCrashFuzzTest"
    "ValidateUserCredentialsInvariantsFuzzTest"
    "ValidateUserCredentialsEmailBoundaryFuzzTest"
    "ValidateUserCredentialsPasswordBoundaryFuzzTest"
    "ValidateUserCredentialsNameBoundaryFuzzTest"
    "ValidateUserCredentialsSpecialCharactersFuzzTest"
    
    # Document Tests
    "ValidateDocumentNoCrashFuzzTest"
    "ValidateDocumentInvariantsFuzzTest"
    "ValidateDocumentTitleFuzzTest"
    "ValidateDocumentContentFuzzTest"
    "ValidateDocumentDateFormatFuzzTest"
    "ValidateDocumentPriorityFuzzTest"
    "ValidateDocumentCategoryFuzzTest"
)

echo "Compiling tests..."
./gradlew :data:compileDebugUnitTestKotlin :domain:compileKotlin -q

echo "Building classpath..."
CLASSPATH=$(./gradlew :data:printTestClasspathFiles -q 2>/dev/null | tail -n1)

# Добавляем Kotlin stdlib
KOTLIN_STDLIB=$(find ~/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlin/kotlin-stdlib -name "*.jar" | head -n 1)
KOTLIN_COROUTINES=$(find ~/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlinx/kotlinx-coroutines-core-jvm -name "*.jar" | head -n 1)

CLASSPATH="$CLASSPATH:$KOTLIN_STDLIB:$KOTLIN_COROUTINES"

mkdir -p fuzz-results

echo "========================================"
echo "Running Complete Fuzz Test Suite"
echo "========================================"
echo ""

PASSED=0
FAILED=0

for test in "${TESTS[@]}"; do
    echo "Running $test..."
    
    jazzer \
      --cp="$CLASSPATH" \
      --target_class=mefetran.dgusev.meddocs.data.usecase.fuzz.$test \
      --instrumentation_includes=mefetran.dgusev.meddocs.** \
      -rss_limit_mb=2048 \
      -max_total_time=30 \
      -artifact_prefix=fuzz-results/$test- \
      > "fuzz-results/$test.log" 2>&1
    
    if [ $? -eq 0 ]; then
        echo "✅ $test PASSED"
        ((PASSED++))
    else
        echo "❌ $test FAILED"
        ((FAILED++))
        echo "   Check fuzz-results/$test.log for details"
    fi
    echo ""
done

echo "========================================"
echo "Test Results Summary"
echo "========================================"
echo "Total tests: $((PASSED + FAILED))"
echo "✅ Passed: $PASSED"
echo "❌ Failed: $FAILED"
echo ""
echo "Results saved in fuzz-results/"
echo "========================================"
